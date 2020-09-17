import {Injectable}                             from '@angular/core';


@Injectable({
    providedIn: 'root',
})
export class SoundServices {

    /**************************************************************************
    * ATTRIBUTES
    **************************************************************************/
    private sounds               : any  = {};

    /**************************************************************************
    * SERVICE
    **************************************************************************/
    public contains(id) : boolean{
        return  isNotNull(this.sounds[id]);
    }

    public load(id,path){
        let sound = this.sounds[id];
        if(isNull(sound)){
            io.inugami.asserts.notNull(path,"Sound path must'nt be null!");
            sound = new Audio();
            sound.src = CONTEXT_PATH+path;
            sound.load();
            this.sounds[id] = sound;
        }
        return sound;
    }


    public loadAll(data){
        io.inugami.asserts.notNull(data,"data is mandatory!");
        for(let soundItem of data){
            this.load(soundItem[0],soundItem[1]);
        }
    }

    public play(id){
        let sound = null;
        if(isNotNull(id)){
           sound = this.sounds[id];
        }

        if(isNotNull(sound) && (sound.played.length==0 || sound.ended)){
            sound.play();
        }
    }
}
