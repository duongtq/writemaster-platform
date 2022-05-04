const mysql = require('sync-mysql');

let mysqlConnection = new mysql({
  host: 'localhost',
  user: 'root',
  password: 'root',
  database: 'writemaster_platform',
});

module.exports = mysqlConnection;
