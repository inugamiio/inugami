NodeMock = function(name){
    this.name=name;
    this._attr={};
    this._groups=[];
    this._parents= [];
}
NodeMock.prototype.attr = function(attrName, value){
    var result = this;
    if(isNull(value)){
        result = this._attr[attrName];
    }else{
        this._attr[attrName]=value;
    }
    return result;
};
NodeMock.prototype.remove = function(attrName){
    delete this._attr[attrName]
    return this;
};
NodeMock.prototype.append = function(nodeType){
    var child = new NodeMock(nodeType);
    this._groups.append(child);
    return child;
};



// ============================================================================
// UNIT TEST
// ============================================================================
QUnit.test("svg.transform.clean", function( assert ) {
    var node = new NodeMock("node");
    node.attr("transform", "translate(10,20)");
    assert.equal( node.attr("transform"),"translate(10,20)", 'node has transform attribute | node.attr("transform", "translate(10,20)") |-> isNotNull(node.attr("transform")' );

    svg.transform.clean(node);
	assert.equal( null,null, 'function has delete attribute | svg.transform.clean(node) |-> isNull(node.attr("transform"))' );
});


QUnit.test("svg.transform.scale", function( assert ) {
    var node = new NodeMock("node");
    var result="";
    assert.ok(node, "node create");
        
    svg.transform.scaleX(node,10);
    
    result="scale(10,0)";
    assert.equal(node.attr("transform"),result,"scale scaleX=10 | svg.transform.scaleX(node,10) |->"+result);

    result="scale(10,20)";
    svg.transform.scaleY(node,20);
    assert.equal(node.attr("transform"),result,"scale scaleY=10 | svg.transform.scaleY(node,20) |->"+result );

    result="scale(30,40)";
    svg.transform.scale(node,30,40);
    assert.equal(node.attr("transform"),result,"scale scaleX=30;scaleY=40 | svg.transform.scale(node,30,40) |->"+result );
    
    result="matrix(30,0,0,40,5,0)";
    svg.transform.translateX(node,5);
    assert.equal( node.attr("transform"),result, "(with matrix) translate x=5 | svg.transform.translateX(node,5) |->"+result );

    result="matrix(10,0,0,40,5,0)";
    svg.transform.scaleX(node,10);
    assert.equal( node.attr("transform"),result, "(with matrix) scaleX=10 | svg.transform.scaleX(node,10) |->"+result );

    result="matrix(10,0,0,20,5,0)";
    svg.transform.scaleY(node,20);
    assert.equal( node.attr("transform"),result, "(with matrix) scaleY=20 | svg.transform.scaleY(node,20) |->"+result );

    result="matrix(30,0,0,40,5,0)";
    svg.transform.scale(node,30,40);
    assert.equal( node.attr("transform"),result, "(with matrix) scaleX=30;scaleY=40 | svg.transform.scale(node,30,40) |->"+result );    

});





QUnit.test("svg.transform.matrix", function( assert ) {
    var node = new NodeMock("node");
    assert.ok(node, "node create");

    svg.transform.matrix(node,0.5, 0.2, 10, 20);
    assert.equal( node.attr("transform"),"matrix(0.5,0,0,0.2,10,20)", "x=10;y=20;scaleX=0.5,scaleY=0.2 | svg.transform.matrix(node,0.5, 0.2, 10, 20) |-> matrix(0.5,0.2,0,0,10,20)" );
});



QUnit.test("svg.transform.translate", function( assert ) {
    var node = new NodeMock("node");
    
    var result="translate(10,0)";
    svg.transform.translateX(node,10);
    assert.equal(node.attr("transform"),result, "translate x=10 | svg.transform.translateX(node,10) |->"+result );

    result="translate(10,20)";
    svg.transform.translateY(node,20);
    assert.equal( node.attr("transform"),result, "translate y=20 | svg.transform.translateX(node,10) |->"+result );

    result="translate(30,40)";
    svg.transform.translate(node,30,40);
    assert.equal( node.attr("transform"),result, "translate x=30;y=40 | svg.transform.translate(node,30,40) |->"+result );

    result="matrix(0.5,0,0,0,30,40)";
    svg.transform.scaleX(node,0.5);
    assert.equal( node.attr("transform"),result, "scaleX 0.5 | svg.transform.scaleX(node,0.5) |->"+result );

    result="matrix(0.5,0,0,0,10,40)";
    svg.transform.translateX(node,10);
    assert.equal( node.attr("transform"),result, "(with matrix) translate x=10 | svg.transform.translateX(node,10) |->"+result );
    
    result="matrix(0.5,0,0,0,10,20)";
    svg.transform.translateY(node,20);
    assert.equal( node.attr("transform"),result, "(with matrix) translate y=20 | svg.transform.translateY(node,20) |->"+result );

    result="matrix(0.5,0,0,0,30,40)";
    svg.transform.translate(node,30,40);
    assert.equal( node.attr("transform"),result, "(with matrix) translate x=30;y=40 | svg.transform.translate(node,30,40) |->"+result );
});


QUnit.test("svg.icons.search", function( assert ) {
    var result = svg.icons.search("fa-android");
    assert.equal("&#x17b",result, "search android | svg.icons.search('fa-android') |->"+result );

    var result = svg.icons.search("fa-android foo-bar");
    assert.equal("&#x17b",result, "search android | svg.icons.search('fa-android foo-bar') |->"+result );

    var result = svg.icons.search("error fa-android foo-bar");
    assert.equal("&#x17b",result, "search android | svg.icons.search('error fa-android foo-bar') |->"+result );

    var result = svg.icons.search("error fa-android");
    assert.equal("&#x17b",result, "search android | svg.icons.search('error fa-android') |->"+result );
});
