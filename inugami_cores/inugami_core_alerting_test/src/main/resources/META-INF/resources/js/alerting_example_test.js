
//##############################################################################
//
//                         SIMPLE ALERT EXAMPLE
//
//##############################################################################

QUnit.test("[alerting_example_test] simple alert", function( assert ) {
    var gav   = mockInugami.buildGav("org.foobar", "my-plugin");
    var event = io.inugami.builders.buildSimpleEvent("simpleEvent","graphite.provider","prd.serice.foo", "-30min");

    var case1 = simpleAlerting(gav, event, mockInugami.data.graphite.simpleData(1));
    assert.notOk(case1 , "[case1] case1 must be null null -> ok" );
    
    var case2 = simpleAlerting(gav, event, mockInugami.data.graphite.simpleData(5.2));
    assert.ok(case2 ,                                "[case2] case2 not null -> ok" );
    assert.equal(case2.level, "warn"                ,"[case2] case2.level = {0} -> equals -> ok".format(case2.level));
    assert.equal(case2.levelType, "WARN"            ,"[case2] case2.levelType = 'WARN' -> equals -> ok");
    assert.equal(case2.duration, 300                ,"[case2] case2.duratio = 300 -> equals -> ok");
    assert.ok(case2.data ,                           "[case2] case2.data not null -> ok" );
    assert.equal(case2.data.service, "my-service"   ,"[case2] case2.data.service = 'my-service' -> equals -> ok");
    assert.equal(case2.data.nominal, "2"            ,"[case2] case2.data.nominal = '2' -> equals -> ok");
    assert.equal(case2.data.unit, "%"               ,"[case2] case2.data.unit = '%' -> equals -> ok");
    assert.equal(case2.data.currentValue, 5.2       ,"[case2] case2.data.currentValue = 5.2  -> equals -> ok");
    
    var case3 = simpleAlerting(gav, event, mockInugami.data.graphite.simpleData(10.1));
    assert.ok(case3 ,                                "[case3] case3 not null -> ok" );
    assert.equal(case3.level, "error"               ,"[case3] case3.level = {0} -> equals -> ok".format(case3.level));
    assert.equal(case3.levelType, "ERROR"           ,"[case3] case3.levelType = {0} -> equals -> ok");
    assert.equal(case3.duration, 300                ,"[case3] case3.duration = {0} -> equals -> ok");
    assert.ok(case3.data ,                           "[case3] case3.data not null -> ok" );
    assert.equal(case3.data.service, "my-service"   ,"[case3] case3.data.service = {0} -> equals -> ok");
    assert.equal(case3.data.nominal, "2"            ,"[case3] case3.data.nominal = {0} -> equals -> ok");
    assert.equal(case3.data.unit, "%"               ,"[case3] case3.data.unit = {0} -> equals -> ok");
    assert.equal(case3.data.currentValue, 10.1      ,"[case3] case3.data.currentValue = 10.1 -> equals -> ok");
});
