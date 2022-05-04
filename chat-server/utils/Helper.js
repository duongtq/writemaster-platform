const crypto = require('crypto');
const algorithm = 'sha256';
const encoding = 'base64';

// SORT USER1 AND USER2 based on id
const getConversationId = (user1, user2) => {
  let keyToHash;
  if (user1.id > user2.id) {
    keyToHash = user2.id + user2.username + user1.id + user1.username;
  } else if (user1.id < user2.id) {
    keyToHash = user1.id + user1.username + user2.id + user2.username;
  } 
  const conversationId = crypto.createHash(algorithm).update(keyToHash).digest(encoding);
  console.log('conversation_id: ', conversationId);
  
  return conversationId;
}

module.exports = { getConversationId }
