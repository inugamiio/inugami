package io.inugami.core.alertings.dynamic.services;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.loggers.Loggers;
import io.inugami.commons.threads.MonitoredThreadFactory;
import io.inugami.core.alertings.dynamic.entities.ActivationTime;
import io.inugami.core.alertings.dynamic.entities.DynamicAlertEntity;
import io.inugami.core.alertings.dynamic.entities.TimeSlot;
import io.inugami.core.context.ApplicationContext;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
@Named
public class DynamicAlertsService implements Serializable {

    private static final String UNDEFINE = "undefine";

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private static final long serialVersionUID = -8151022786358668308L;

    //@formatter:off
    private static final String[] DAYS = {"SUNDAY",
            "MONDAY",
            "TUESDAY",
            "WEDNESDAY",
            "THURSDAY",
            "FRIDAY",
            "SATURDAY"};
    //@formatter:on

    @Inject
    private transient ApplicationContext context;

    private transient ExecutorService executor;

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public DynamicAlertsService() {
    }

    protected DynamicAlertsService(final ApplicationContext context) {
        this.context = context;
        postConstruct();
    }

    @PostConstruct
    public void postConstruct() {
        final int maxThreads = context.getApplicationConfiguration().getAlertingDynamicMaxThreads();

        //@formatter:off
        executor = Executors.newFixedThreadPool(maxThreads,
                                                new MonitoredThreadFactory(DynamicAlertsService.class.getSimpleName(), false));

        //@formatter:on
    }

    @PreDestroy
    public void shutdown() {
        executor.shutdown();
    }

    // =========================================================================
    // METHODS
    // =========================================================================
    public void process(final List<DynamicAlertEntity> entities) {
        Asserts.assertNotNull(entities);

        final List<DynamicAlertEntity> alertsToProcess = resolveAlertsToProcess(entities, buildCurrentTime());

        if (!alertsToProcess.isEmpty()) {
            Loggers.ALERTING.info("{} dynamic alerts to process", alertsToProcess.size());
            final List<DynamicAlertsTask> tasks = buildTasks(alertsToProcess);

            processAlerting(tasks);
        }
    }

    // =========================================================================
    // RESOLVE ALERTS TO PROCESS
    // =========================================================================
    protected List<DynamicAlertEntity> resolveAlertsToProcess(final List<DynamicAlertEntity> entities,
                                                              final long timestamp) {
        final List<DynamicAlertEntity> result = new ArrayList<>();

        for (final DynamicAlertEntity entity : entities) {
            final String       cronExpression = resolveCronExpression(entity);
            final CronResolver cronResolver   = buildCronREsolver(cronExpression, entity.getUid());

            if ((cronExpression != null) && cronResolver.willFire(timestamp)
                    && alertIsInTimeSlot(entity.getActivations(), timestamp)) {
                result.add(entity);
            }
        }
        return result;
    }

    protected boolean alertIsInTimeSlot(final List<ActivationTime> activations, final long timestamp) {
        boolean result = false;

        final List<ActivationTime> currentDayActivation = searchCurrentDayActivation(activations, timestamp);
        if (!currentDayActivation.isEmpty()) {
            final int hour = buildHour(timestamp);
            for (final ActivationTime activationTime : currentDayActivation) {
                result = isActivationInTimeSlot(activationTime, hour);
                if (result) {
                    break;
                }
            }
        }
        return result;
    }

    private List<ActivationTime> searchCurrentDayActivation(final List<ActivationTime> activations,
                                                            final long timestamp) {
        final List<ActivationTime> result     = new ArrayList<>();
        final int                  currentDay = buildCurrentDay(timestamp);

        if (activations != null) {
            for (final ActivationTime activation : activations) {
                final Set<Integer> days = convertToDayIndex(activation.getDays());
                if (days.contains(currentDay)) {
                    result.add(activation);
                }
            }
        }

        return result;
    }

    private Set<Integer> convertToDayIndex(final List<String> activationDays) {
        final Set<Integer> days = new HashSet<>();
        if (activationDays != null) {
            //@formatter:off
            activationDays.stream()
                          .map(this::convertToDayIndex)
                          .forEach(days::add);
            //@formatter:on
        }
        return days;
    }

    private boolean isActivationInTimeSlot(final ActivationTime activationTime, final int hour) {
        boolean result = false;

        if (activationTime.getHours() != null) {
            for (final TimeSlot timeSlot : activationTime.getHours()) {
                final int from = convertToFromHour(timeSlot.getFrom());
                final int to   = convertToUntilHour(timeSlot.getTo());
                result = (hour >= from) && (hour < to);
                if (result) {
                    break;
                }
            }
        }
        return result;
    }

    private int convertToDayIndex(final String day) {
        int result = -1;
        if (day != null) {
            final String currentDay = day.trim();
            for (int i = 0; i < DAYS.length; i++) {
                if (DAYS[i].equalsIgnoreCase(currentDay)) {
                    result = i + 1;
                    break;
                }
            }
        }
        return result;

    }

    private int buildCurrentDay(final long timestamp) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.DAY_OF_WEEK);
    }

    private int buildHour(final long timestamp) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timestamp);
        return calendar.get(Calendar.HOUR_OF_DAY);
    }

    private int convertToFromHour(final String from) {
        int result = -1;
        if ((from != null) && (from.length() == 5) && from.contains(":")) {
            final String[] parts = from.split(":");
            if (parts.length == 2) {
                try {
                    result = Integer.parseInt(parts[0]);
                } catch (final NumberFormatException e) {
                    Loggers.DEBUG.error(e.getMessage(), e);
                }

            }
        }
        return result;
    }

    private int convertToUntilHour(final String to) {
        final int hour = convertToFromHour(to);
        return hour == 0 ? 24 : hour;
    }

    // =========================================================================
    // PROCESS ALERTING
    // =========================================================================
    private void processAlerting(final List<DynamicAlertsTask> tasks) {
        tasks.forEach(executor::submit);
    }

    // =========================================================================
    // TOOLS
    // =========================================================================
    private String resolveCronExpression(final DynamicAlertEntity entity) {
        String result = null;
        if ((entity.getSource() != null) && (entity.getSource().getCronExpression() != null)) {
            result = context.getGlobalConfiguration().applyProperties(entity.getSource().getCronExpression());
        }

        return result;
    }

    private CronResolver buildCronREsolver(final String cronExpression, final String entityUid) {
        CronResolver result = null;
        if (cronExpression != null) {
            try {
                result = new CronResolver(cronExpression);
            } catch (final FatalException e) {
                final String message = String.format("unable to resolve cron expression %s from dynamic alert %s",
                                                     cronExpression, entityUid);
                Loggers.DEBUG.error(message, e);
                Loggers.ALERTS_SENDER.error(message);
            }

        }

        return result;
    }

    private List<DynamicAlertsTask> buildTasks(final List<DynamicAlertEntity> alertsToProcess) {
        final List<DynamicAlertsTask> result = new ArrayList<>();

        for (final DynamicAlertEntity entity : alertsToProcess) {
            result.add(new DynamicAlertsTask(entity.cloneObject(), context));
        }
        return result;
    }

    private long buildCurrentTime() {
        final Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MILLISECOND, 0);
        calendar.set(Calendar.SECOND, 0);
        return calendar.getTimeInMillis();
    }
}
