//==================================================================
// DATA
//==================================================================
const contentType = {
    JSON_UTF8:"application/json; charset=utf-8"
}

//==================================================================
// UTILS
//==================================================================
const utils = {
    _S4 : () =>{
        return (((1+Math.random())*0x10000)|0).toString(16).substring(1);
    },
   buildUid : ()=>{
         return (utils._S4() + utils._S4() + "-" + utils._S4() + "-4" + utils._S4().substr(0,3) + "-" + utils._S4() + "-" + utils._S4() + utils._S4() + utils._S4()).toLowerCase();
    },
    searchByName: (data, name)=> {
        if(data != undefined && data!= null){
            for(var i=0; i< data.length; i++){
                if(data[i].name == name){
                    return  data[i].id;
                }
            }
        }
        return null;
    },

    searchByEmail : (data, email)=> {
        if(data != undefined && data!= null){
            for(var i=0; i< data.length; i++){
                if(data[i].email == email){
                    return  data[i].id;
                }
            }
        }
        return null;
    },

    now : (dayOffset) => {
        if(dayOffset==undefined || dayOffset == null){
            return new Date().toISOString().split('T')[0];
        }else{
            return new Date(new Date().setHours(new Date().getHours()+(24*dayOffset))).toISOString().split('T')[0];
        }
        
    },
    tomorrow : (dayOffset) => {
        if(dayOffset==undefined || dayOffset == null){
            return new Date(new Date().setHours(new Date().getHours()+24)).toISOString().split('T')[0];
        }else{
            return new Date(new Date().setHours(new Date().getHours()+24+(24*dayOffset))).toISOString().split('T')[0];
        }
        
    },
    
}

//==================================================================
// EXPORT
//==================================================================
module.exports = {
  contentType,
  utils
}