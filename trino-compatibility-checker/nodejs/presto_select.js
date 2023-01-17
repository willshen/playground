var util = require('util')
var presto = require('presto-client');
var client = new presto.Client({host: 'localhost', port: 8080, user: 'nodejs-client', catalog: 'hive', schema: 'schema'});
const tables = ['table1'];

tables.forEach(table => { 
  var sql = 'SELECT * FROM ' + table + ' LIMIT 1';
  console.log('Querying: ' + sql);
  client.execute({
    query: sql,
    data: function(error, data, columns, stats){
      console.log(util.inspect(columns, {breakLength: Infinity, maxArrayLength: null, depth:null}));
      console.log(util.inspect(data, {breakLength: Infinity, maxArrayLength: null, depth:null }));
    },
    success: function(error, stats){},
    error:   function(error){}
  });
});
