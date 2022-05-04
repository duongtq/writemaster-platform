const { ObjectID } = require('bson');
const mongoose = require('mongoose');

const ConversationSchema = new mongoose.Schema({
  conversation_id: {
    type: String // the hash of username1 + username2
  },
  messengers: [
    {
      id: {
        type: Number,
        required: true,
      },
      username: {
        type: String,
        required: true,
      }
    }
  ],
  messages: [{
    from: {
      type: String,
      required: true
    },
    to: {
      type: String,
      required: true
    },
    content: {
      type: String,
      required: true
    },
    sent_at: {
      type: Date,
      required: true
    },
  }
  ]
});

const Conversation = mongoose.model('Conversation', ConversationSchema);

module.exports = Conversation;

