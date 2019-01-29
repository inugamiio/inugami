/* --------------------------------------------------------------------
 *  Inugami  
 * --------------------------------------------------------------------
 * 
 * This program is free software: you can redistribute it and/or modify  
 * it under the terms of the GNU General Public License as published by  
 * the Free Software Foundation, version 3.
 *
 * This program is distributed in the hope that it will be useful, but 
 * WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU 
 * General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License 
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package org.inugami.core.cdi.services.dao;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang.StringEscapeUtils;
import org.inugami.api.dao.Dao;
import org.inugami.api.dao.DaoEntityNotFoundException;
import org.inugami.api.dao.DaoEntityNullException;
import org.inugami.api.dao.DaoException;
import org.inugami.api.dao.Identifiable;
import org.inugami.api.exceptions.Asserts;
import org.inugami.api.exceptions.TechnicalException;
import org.inugami.commons.security.SecurityTools;
import org.inugami.commons.tools.ProxyBuilder;
import org.picketlink.Identity;

import flexjson.JSONDeserializer;
import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * AbstractCrudRest
 * 
 * @author patrick_guillerm
 * @since 9 janv. 2018
 */
public abstract class AbstractCrudRest<E extends Identifiable<PK>, PK extends Serializable> {
    
    // =========================================================================
    // ATTRIBUTES
    // =========================================================================
    @Inject
    private Dao                          dao;
    
    @Inject
    private Identity                     identity;
    
    private final Class<? extends E>     type;
    
    private final CrudSecurityHandler<E> securityHandler;
    
    private final PostCrudHandler<E>     handler;
    
    private final Callback               callback = new MethodInterceptor() {
                                                      @Override
                                                      public Object intercept(final Object obj, final Method method,
                                                                              final Object[] args,
                                                                              final MethodProxy proxy) throws Throwable {
                                                          // Nothing to do
                                                          return null;
                                                      }
                                                  };
    
    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================
    public AbstractCrudRest() {
        super();
        this.type = initType();
        final CrudSecurityHandler<E> secuHandler = initSecurityHandler();
        final PostCrudHandler<E> crudHandler = initHandler();
        this.securityHandler = secuHandler == null ? createGrantAllProxy() : secuHandler;
        this.handler = crudHandler == null ? createHandlerProxy() : crudHandler;
    }
    
    private PostCrudHandler<E> createHandlerProxy() {
        return new ProxyBuilder<PostCrudHandler<E>>().addInterface(PostCrudHandler.class).addCallback(callback).build();
    }
    
    private CrudSecurityHandler<E> createGrantAllProxy() {
        return new ProxyBuilder<CrudSecurityHandler<E>>().addInterface(CrudSecurityHandler.class).addCallback(callback).build();
    }
    
    protected abstract Class<? extends E> initType();
    
    protected abstract PK parseUid(final String uid) throws DaoException;
    
    protected CrudSecurityHandler<E> initSecurityHandler() {
        return null;
    }
    
    protected PostCrudHandler<E> initHandler() {
        return null;
    }
    
    // =========================================================================
    // METHODS GET/FIND/COUNT
    // =========================================================================
    /**
     * [GET: {entityName}/ ] will return all entities
     * 
     * @return entities saved
     * @throws DaoException if exception is occurs
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<E> find() throws DaoException {
        securityHandler.onFindAll(identity);
        final List<E> daoResult = dao.findAll(type);
        final List<E> result = daoResult == null ? new ArrayList<>() : daoResult;
        handler.onFindAll(result);
        return secureXssEntities(result);
    }
    
    //@formatter:off
    /**
     * [GET: {entityName}/find?first=0&pageSize=10&field=filedNameForOrdering&sort=ASC&filter={'foo':'bar'} ] will return all entities found
     * @param  first  first element index
     * @param  pageSize how many elements will be return by query 
     * @param  field When ordering by field, this value will specify which field DAO must ordering.
     * @param  sort will define sort ordering, ASC or DSC
     * @param  filters specific filters on entity field
     * @return list of entity found
     * @throws DaoException if exception is occurs
     */
    @GET
    @Path("/find")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<E> find(@QueryParam("first") final    int first,
                        @QueryParam("pageSize") final int pageSize,
                        @QueryParam("field") final    String field,
                        @QueryParam("sort") final     String sort,
                        @QueryParam("fitlers") final  String filtersJson) throws DaoException {
        //@formatter:on
        final String securField = SecurityTools.checkInjection(field);
        final String secureSort = SecurityTools.checkInjection(sort);
        final String secureFiltersJson = SecurityTools.checkInjection(filtersJson);
        
        securityHandler.onFind(identity);
        final Map<String, String> filters = convertFilters(secureFiltersJson);
        final List<E> result = dao.find(type, first, pageSize, securField, secureSort, filters);
        handler.onFind(result);
       
        return  secureXssEntities(result);
    }
    
    /**
     * [GET: {entityName}/count?filter={'foo':'bar'}] Will count entities
     * stored.
     * 
     * @param filters optional field, for filter on entity field.
     * @return how many entities saved
     * @throws DaoException if exception is occurs.
     */
    @GET
    @Path("/count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public int count(@QueryParam("fitlers")
    final String rawFilters) throws DaoException {
        SecurityTools.checkInjection(rawFilters);
        securityHandler.onCount(identity);
        int result = 0;
        
        if (rawFilters == null) {
            result = dao.count(type);
        }
        else {
            final Map<String, String> filters = convertFilters(rawFilters);
            result = dao.count(filters, type);
        }
        handler.onCount(result);
        return result;
    }
    
    /**
     * [GET: {entityName}/{uid} ] will return entity found with speicfied uid
     * 
     * @param uid entity uid
     * @return entity or null
     * @throws TechnicalException
     */
    @GET
    @Path("{uid}")
    @Produces(MediaType.APPLICATION_JSON)
    public E get(@PathParam("uid")
    final String uid) throws TechnicalException {
        SecurityTools.checkInjection(uid);
        securityHandler.onGet(identity, uid);
        DaoEntityNullException.assertNotNull(uid);
        final E result = dao.getByUid(type, parseUid(uid));
        if (result == null) {
            throw new DaoEntityNotFoundException("can't found entity {0} with uid {1}", type.getSimpleName(), uid);
        }
        handler.onGet(result);
        
        return secureXssEntity(result);
    }
    
    // =========================================================================
    // METHODS SAVE/MERGE
    // =========================================================================
    /**
     * [POST: {entityName}/ | body : [entities] ] will save all entities
     * 
     * @param listEntity entities to save
     * @return all entities uid saved
     * @throws DaoException if exception is occurs
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> save(final List<E> listEntity) throws DaoException {
        securityHandler.onSave(identity, listEntity);
        DaoEntityNullException.assertNotNull(listEntity);
        dao.save(listEntity, type);
        handler.onSave(listEntity);
        //@formatter:off
        return listEntity.stream()
                         .map(Identifiable::getUid)
                         .map(String::valueOf)
                         .collect(Collectors.toList());
        //@formatter:on
    }
    
    /**
     * [PUT: {entityName}/ | body : [entities] ] will merge all entities
     * 
     * @param listEntity entities to merge
     * @return all merged entities
     * @throws DaoException if exception is occurs
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<E> merge(final List<E> listEntity) throws DaoException {
        securityHandler.onMerge(identity, listEntity);
        DaoEntityNullException.assertNotNull(listEntity);
        dao.merge(listEntity, type);
        handler.onMerge(listEntity);
       
        return  secureXssEntities(listEntity);
    }
    
    /**
     * [POST: {entityName}/register | body : [entities] ] will save all entities
     * 
     * @param listEntity entities to save
     * @return all entities uid saved
     * @throws DaoException if exception is occurs
     */
    @Path("register")
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public List<String> register(final List<E> entities) throws DaoException {
        securityHandler.onRegister(identity, entities);
        DaoEntityNullException.assertNotNull(entities);
        dao.register(entities, type);
        handler.onRegister(entities);
        
        //@formatter:off
        return entities.stream()
                       .map(Identifiable::getUid)
                       .map(String::valueOf)
                       .collect(Collectors.toList());
        //@formatter:on
    }
    
    protected List<PK> extractUids(final List<E> listEntity) {
        //@formatter:off
        return Optional.ofNullable(listEntity).orElse(new ArrayList<>())
                       .stream()
                       .map(Identifiable::getUid)
                       .collect(Collectors.toList());
        //@formatter:on
    }
    
    // =========================================================================
    // METHODS DELETE
    // =========================================================================
    /**
     * [DELETE: {entityName}/{uid}] will delete specified entity
     * 
     * @param uid entity uid
     * @return void or error
     * @throws TechnicalException if exception is occurs
     */
    @Path("{uid}")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(@PathParam("uid")
    final String uid) throws TechnicalException {
        SecurityTools.checkInjection(uid);
        securityHandler.onDelete(identity, uid);
        DaoEntityNullException.assertNotNull(uid);
        final PK realUid = parseUid(uid);
        dao.delete(realUid, type);
        handler.onDeleteItem(uid);
        return buildResponse(Response.Status.OK);
    }
    
    /**
     * [DELETE: {entityName}/ | body : [uids] ] will deletes all entities
     * 
     * @param uid entity uid
     * @return void or error
     * @throws TechnicalException if exception is occurs
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(final List<String> uids) throws TechnicalException {
        //@formatter:off
        Optional.ofNullable(uids)
                .orElseGet(Collections::emptyList)
                .forEach(SecurityTools::checkInjection);
        //@formatter:off
               securityHandler.onDelete(identity, uids);
        DaoEntityNullException.assertNotNull(uids);
        
        final List<PK> realUids = new ArrayList<>();
        for (final String uid : uids) {
            realUids.add(parseUid(uid));
        }
        
        dao.deleteByIds(realUids, type);
        handler.onDelete(uids);
        return buildResponse(Response.Status.OK);
    }
    
    // =========================================================================
    // TOOLS
    // =========================================================================
    protected Response buildResponse(final Response.Status status) {
        return Response.status(status).build();
    }
    
    protected Map<String, String> convertFilters(final String filtersJson) {
        Map<String, String> result = null;
        if (filtersJson != null) {
            result = new JSONDeserializer<Map<String, String>>().deserialize(filtersJson);
        }
        return result;
    }
    
    
    // =========================================================================
    // SECURITY
    // =========================================================================
    protected abstract E secureXssEntity(E entity);
    
    private List<E> secureXssEntities(final List<E> entities) {
        List<E> result = null;
        if (entities != null) {
            result = new ArrayList<>();
            for (final E entity : entities) {
                result.add(secureXssEntity(entity));
            }
        }
        return result;
    }
    

    
    // =========================================================================
    // GETTERS
    // =========================================================================
    protected Dao getDao() {
        return dao;
    }
}
