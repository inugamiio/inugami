import {Injectable}                              from '@angular/core';
import {Http, Response,Headers, RequestOptions}  from '@angular/http';
import {HttpServices}                            from './http.services'

@Injectable()
export abstract class GenericCrudServices<T> {

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private url             : string;

    /**************************************************************************
    * CONSTRUCTORS
    **************************************************************************/
    constructor(private httpServices: HttpServices) {
        this.url = CONTEXT_PATH+ this.getServiceUrl();
        this.postConstruct();

    }

    /**************************************************************************
    * ABSTRACT
    **************************************************************************/
    getServiceUrl(){};
    postConstruct(){};


    /**************************************************************************
    * READ
    **************************************************************************/
    public findAll() : Promise<T[]>{
        return this.httpServices.get(this.url);
    }


    public find(first,pageSize,field,sort,filters) : Promise<T[]>{
        let url = [this.url,"/find"];


        if(isNotNull(first||pageSize||field||sort||filters)){
            url.push("?");
        }
        
        if(isNotNull(first)){
            url.push("first=");
            url.push(first);
        }
        
        if(isNotNull(pageSize)){
            url.push("&pageSize=");
            url.push(pageSize);
        }
        
        if(isNotNull(sort)){
            url.push("&sort=");
            url.push(sort);
        }
        
        if(isNotNull(field)){
            url.push("&field=");
            url.push(field);
        }
        
        if(isNotNull(filters)){
            url.push("&filters=");
            url.push(JSON.stringify(filters));
        }
        return this.httpServices.get(url.join(""));
    }

    public count(filters) :  Promise<number>{
        let url = [this.url,"/find"];
        if(isNotNull(filters)){
            url.push("?filters=");
            url.push(JSON.stringify(filters));
        }
        return this.httpServices.get(url.join(""));
    }

    public get(uid) : Promise<T>{
        let fullUrl = [this.url, uid].join("/");
        return this.httpServices.get(fullUrl);
    }

    /**************************************************************************
    * CREATE/UPDATE
    **************************************************************************/
    public save(entities:T[]) : Promise<string[]>{
        return this.httpServices.post(this.url,entities,{'Content-Type': 'application/json'});
    }

    public merge(entities:T[]) : Promise<T[]>{
        return this.httpServices.put(this.url,entities,{'Content-Type': 'application/json'});
    }

    /**************************************************************************
    * DELETE
    **************************************************************************/
    public delete(uids :string[]): Promise<any>{
        return this.httpServices.delete(this.url,uids,{'Content-Type': 'application/json'});
    }

}
