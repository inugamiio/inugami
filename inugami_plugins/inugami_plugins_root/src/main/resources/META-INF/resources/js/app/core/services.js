// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// CORE SERVICE
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
io.inugami.services = {

    generateId: function (componentName) {
        var index = io.inugami._innerValues.componentsIds[componentName];

        if (index === undefined || index === null) {
            index = 0;
        }
        index++;
        io.inugami._innerValues.componentsIds[componentName] = index;
        return componentName + "_" + index;
    },

    getIdOrGeneratedId : function (id, componentName){
        var result = null;
        if(io.inugami.check.isNull(id)) {
            io.inugami.asserts.notNull(componentName,"component name mustn't be null!");
            result =io.inugami.services.generateId(componentName);
        } else{
            io.inugami.asserts.notNull(id,"component id mustn't be null!");
            result = id;
        }
        return result;
    },

    normalizeId: function (value) {
        TAB_00C0 = "AAAAAAACEEEEIIIIDNOOOOO*OUUUUYIs" +
        "aaaaaaaceeeeiiii?nooooo/ouuuuy?y" +
        "AaAaAaCcCcCcCcDdDdEeEeEeEeEeGgGg" +
        "GgGgHhHhIiIiIiIiIiJjJjKkkLlLlLlL" +
        "lLlNnNnNnnNnOoOoOoOoRrRrRrSsSsSs" +
        "SsTtTtTtUuUuUuUuUuUuWwYyYZzZzZzF";
        var result = value.split('');
        for (var i = 0; i < result.length; i++) {
            var c = value.charCodeAt(i);
            if (c >= 0x00c0 && c <= 0x017f) {
                result[i] = String.fromCharCode(TAB_00C0.charCodeAt(c - 0x00c0));
            } else if (c > 127) {
                result[i] = '_';
            }
        }

        return result.join('').replace(/\W/g, '_');
    },



    defaultValueEmpty: function (value) {
        return io.inugami.services.defaultValue(value, "");
    },

    defaultValue: function (value, defaultValue) {
        var isEmpty = value === undefined;

        if (!isEmpty && (typeof value === 'string' || value instanceof String)) {
            isEmpty = "" === value;
        }

        return isEmpty ? defaultValue : value;
    },

    getParent: function (nodeName, currentNode) {
        io.inugami.asserts.notNull(nodeName, "can't search node parent without this name!")
        return this._processGetParent(nodeName.toUpperCase(), currentNode);
    },

    _processGetParent: function (nodeName, currentNode) {
        var result = null;

        var parent = null;
        if (currentNode !== null) {
            parent = currentNode.context === undefined ? currentNode.parentNode : currentNode.context.parentNode;
        }
        if (parent !== null) {
            if (nodeName === parent.nodeName) {
                result = parent;
            } else {
                result = this._processGetParent(nodeName, parent);
            }
        }
        return result;
    },

    getFunction: function (name) {
        io.inugami.asserts.notNull(name, "function name mustn't be null !");
        var result = null;

        if (name.indexOf(".") == -1) {
            result = window[name];
        } else {
            var packageNames = name.split('.');

            for (var index = 0; index < packageNames.length; index++) {
                if (index == 0) {
                    result = window[packageNames[index]];
                    io.inugami.asserts.notNull(result, "no namespace found :" + packageNames[index]);
                } else {
                    result = result[packageNames[index]];
                }
            }
        }

        return result;
    },

    removeFromList : function(value, list, functionEquals){
        io.inugami.asserts.notNull(value, "value mustn't be null");
        io.inugami.asserts.notNull(list, "list values mustn't be null");
        io.inugami.asserts.notNull(functionEquals, "equals function mustn't be null");

        var index = io.inugami.checks.indexOf(value, list,functionEquals);
        if(index >= 0) {
            list.splice(index, 1);
        }

        return list;
    }
};


// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
// CORE SERVICE GENERATORS
// :::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
io.inugami.services.generators = {
    lorem : function(sizeMin, sizeMax){
        var nbWord = isNull(sizeMax)?sizeMin:io.inugami.services.generators.randomNumber(sizeMin, sizeMax);
        if(isNull(nbWord) || nbWord<=0){
            nbWord = 1;
        }
        var result = [];
        for(var i=0; i<nbWord; i++){
            var index =io.inugami.services.generators.randomNumber(0, io.inugami.constants.lorem.size);
            var item = io.inugami.constants.lorem.data[index];
            result.push(item);
        }
        return result.join(" ");
    },
    randomBoolean : function(){
        return io.inugami.services.generators.randomNumber(0,10)%2==0;
    },
    randomNumber  : function(min, max){
        var currentMin = Math.ceil(min);
        var currentmax = Math.floor(max);
        return Math.floor(Math.random() * (currentmax - currentMin)) + currentMin;
    },
    randomValues : function(values){
        var size = values.length;
        return values[io.inugami.services.generators.randomNumber(0,size)];
    }   
};
