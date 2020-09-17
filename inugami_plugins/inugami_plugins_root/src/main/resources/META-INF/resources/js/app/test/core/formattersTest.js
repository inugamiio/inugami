
/**
 * 
 */
QUnit.test("io.inugami.formatters.truncateNumber", function( assert ) {
	var value = null;

	value = io.inugami.formatters.truncateNumber(12345, 1);
	assert.equal( value,"12.3K", "12.3K -> Passed!" );
	
	value = io.inugami.formatters.truncateNumber(1000);
	assert.equal( value,"1K", "1K -> Passed!" );
	
	value = io.inugami.formatters.truncateNumber(1234);
	assert.equal( value,"1K", "1K -> Passed!" );

	value = io.inugami.formatters.truncateNumber(1234, 1);
	assert.equal( value,"1.2K", "1.2K -> Passed!" );
  

	value = io.inugami.formatters.truncateNumber(1234567, 1);
	assert.equal( value,"1.2M", "1.2M -> Passed!" );

	  
	value = io.inugami.formatters.truncateNumber(1245678901, 1);
	assert.equal( value,"1.2G", "1.2G -> Passed!" );

	value = io.inugami.formatters.truncateNumber(4512345678901, 1);
	assert.equal( value,"4.5T", "4.5T -> Passed!" );


	value = io.inugami.formatters.truncateNumber(200, 1);
	assert.equal( value,"200.0", "200.0 -> Passed!" );
	
	value = io.inugami.formatters.truncateNumber(200);
	assert.equal( value,"200", "200 -> Passed!" );


	value = io.inugami.formatters.truncateNumber(0.123, 1);
	assert.equal( value,"0.1", "0.1 -> Passed!" );
	
	value = io.inugami.formatters.truncateNumber(0.123, 2);
	assert.equal( value,"0.12", "0.12 -> Passed!" );

});