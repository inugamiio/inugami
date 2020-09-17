package io.inugami.core.alertings.dynamic.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import io.inugami.api.exceptions.Asserts;
import io.inugami.api.exceptions.FatalException;
import io.inugami.api.loggers.Loggers;
import org.quartz.CronExpression;

public class CronResolver {
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    private final String         expression;
    
    private final CronExpression expr;
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public CronResolver(final String expression) {
        super();
        this.expression = expression;
        
        try {
            expr = new CronExpression(expression);
        }
        catch (final ParseException e) {
            throw new FatalException(e.getMessage(), e);
        }
    }
    
    // =========================================================================
    // METHODS
    // =========================================================================
    public boolean willFire(final long date) {
        final Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(date);
        return willFire(calendar);
    }
    
    public boolean willFire(final Calendar date) {
        Asserts.notNull("date is mandatory!", date);
        Loggers.DEBUG.trace("check for {} -> {}", expression, format(date));
        
        return expr.isSatisfiedBy(date.getTime());
    }
    
    private String format(final Calendar date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(date.getTimeInMillis()));
    }
}
