import {Observable,Subscriber} from 'rxjs'

export class ObservableEvent<T>{
    handler : Subscriber = null;
    observer : Observable<T> =null;

    constructor(){
        this.observer= new Observable<T>((handlerEvent)=>{
            this.handler=handlerEvent;
        });
    }

    getHandler():Subscriber{
        return this.handler;
    }

    getObserver():Observable<T>{
        return this.observer;
    }

    fireEvent(data:T){
        if(this.handler!=null){
            this.handler.next(data);
        }
    }
}