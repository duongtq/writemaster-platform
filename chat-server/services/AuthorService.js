const mysql = require('../db/MySQLConnection');

const AuthorService = {
  getAllAuthors: () => {
    const authors = mysql.query('SELECT * FROM authors');
    return authors.map(ele => {
      return {
        id: ele.id,
        firstName: ele.first_name,
        lastName: ele.last_name,
        username: ele.username,
        avatar_url: ele.avatar_url,
        status: 'offline'
      }
    })
  }
}

module.exports = AuthorService;
