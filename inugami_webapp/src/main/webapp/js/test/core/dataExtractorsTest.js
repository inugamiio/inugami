
QUnit.test("org.inugami.datas.extractors.graphite.cleanDatapoints",
		function(assert) {
			var data = [];
			data.push({
				'value' : null,
				timestamp : 1
			});
			data.push({
				'value' : 5.0,
				timestamp : 2
			});
			data.push({
				'value' : null,
				timestamp : 3
			});
			data.push({
				'value' : 12.0,
				timestamp : 4
			});
			data.push({
				'value' : 24.123,
				timestamp : 5
			});

			var cleanData = org.inugami.datas.extractors.graphite
					.cleanDatapoints(data);
			assert.notOk(cleanData == null, "cleanData not null -> ok");
			assert.equal(3, cleanData.length, "new data has 3 items -> ok");
			assert.equal(5.0, cleanData[0].value, "first items -> ok");
			assert.equal(12.0, cleanData[1].value, "second items -> ok");
			assert.equal(24.123, cleanData[2].value, "third items -> ok");
		});

QUnit.test("org.inugami.data.extractors.graphite.sortTargets",
		function(assert) {
			var data = [];
			data.push({
				'target' : 'foo',
				datapoints : []
			});
			data.push({
				'target' : 'bar',
				datapoints : []
			});
			data.push({
				'target' : 'joe',
				datapoints : []
			});

			var result = org.inugami.data.extractors.graphite
					.sortTargets(data);
			assert.notOk(result == null, "result not null -> ok");
			assert.equal(3, result.length, "result has 3 items -> ok");
			assert.equal(result[0].target, "bar", "item 0 has 'bar' -> ok");
			assert.equal(result[1].target, "foo", "item 1 has 'bar' -> ok");
			assert.equal(result[2].target, "joe", "item 2 has 'bar' -> ok");

		});

QUnit.test("org.inugami.data.extractors.style.decomposeStyleClass", function(
		assert) {

	var styleClass = org.inugami.data.extractors.style.decomposeStyleClass(
			'foo-bar', '-');
	assert.notOk(styleClass == null, "styleClass not null   -> ok");
	assert.equal(styleClass, "foo bar", "convert to 'foo bar'  -> ok");

	var styleClassB = org.inugami.data.extractors.style.decomposeStyleClass(
			'foo_bar', '_');
	assert.notOk(styleClassB == null, "styleClass not null   -> ok");
	assert.equal(styleClassB, "foo bar", "convert to 'foo bar'  -> ok");

	var styleClassC = org.inugami.data.extractors.style.decomposeStyleClass(
			'foo_bar', null);
	assert.notOk(styleClassC == null, "styleClass not null   -> ok");
	assert.equal(styleClassC, "foo_bar", "convert to 'foo_bar'  -> ok");

	var styleClassD = org.inugami.data.extractors.style.decomposeStyleClass(
			null, null);
	assert.notOk(styleClassD == null, "styleClass not null   -> ok");
	assert.equal(styleClassD, "", "convert to ''  -> ok");
});