"use strict";process.argv.length<3&&process.exit(1);var directoryName=process.argv[2],fs=require("fs");try{var watcher=fs.watch(directoryName,{recursive:!0},function(){return{}});watcher.close()}catch(r){}process.exit(0);