import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Comment from './Comment';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faPaperPlane } from '@fortawesome/free-solid-svg-icons';
import { List, Modal } from 'antd';

const CommentList = ({ post, commentCount, setCommentCount }) => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));
  const authorId = currentUser.id;
  const postId = post.id;

  // console.log('post ', post);

  const [commentList, setCommentList] = useState(post.comments);
  // const [comment, setComment] = useState('');

  // console.log('comment list ', commentList);

  useEffect(() => {
    axios.get(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/comments/post/${postId}`, {
      headers: {
        Authorization: `Bearer ${currentUser.token}`
      }
    }).then(res => {
      setCommentList(res.data);
      setCommentCount(res.data.length);
      // document.getElementById('new-comment').value = '';
    }).catch(err => {
      console.log(err);
    })
  }, []);

  const updateList = () => {
    axios.get(`http://localhost:8080/api/v1/comments/post/${postId}`, {
      headers: {
        Authorization: `Bearer ${currentUser.token}`
      }
    }).then(res => {
      setCommentList(res.data);
      setCommentCount(res.data.length);
      // document.getElementById('new-comment').value = '';
    }).catch(err => {
      console.log(err);
    })
  }

  const addNewComment = () => {
    // console.log('New comment: ', newComment);

    if (!document.getElementById(post.id).value) {
      console.log('Empty comment');
      alert('Your comment, as your mind, should never be empty. :)');
      return;
    }

    // COUNT WORD
    const words = document.getElementById(post.id).value.split(" ");
    console.log('word: ', words);
    if (words.length > 80) {
      const modal = Modal.error({
        title: 'Error',
        content: 'Long comment. Maximum length is 80 words.'
      })
      modal.update();
      return;
    }

    const createCommentPayload = {
      content: document.getElementById(post.id).value,
      createdOn: new Date()
    }

    document.getElementById(post.id).value = '';

    console.log('new comment payload: ', createCommentPayload);

    axios.post(`http://localhost:8080/api/v1/comments/author/${authorId}/post/${postId}`, createCommentPayload, {
      headers: {
        Authorization: `Bearer ${currentUser.token}`
      }
    }).then(res => {
      // console.log('New comment added ');
      console.log(res.data);

      updateList();

      // setCommentList([...commentList, res.data]);

      // setComment('');

    }).catch(err => {
      // console.log('Error add comments');
    });

  }

  return (
    <div className='mt-3 mb-3'>
      {
        commentList.length !== 0
          ? <div className='comment-list mt-4 mb-4'>
            <List
              itemLayout='vertical'
              size='large'
              pagination={{
                onChange: page => {
                  console.log(page);
                },
                pageSize: 5,
                style: {
                  textAlign: 'center',
                  marginTop: '2em',
                  marginBottom: '2em'
                }
              }}
              dataSource={commentList.sort((a, b) => {
                if (a.commentId > b.commentId) {
                  return 1;
                }
                if (a.commentId < b.commentId) {
                  return -1;
                }
                return 0;
              })}
              renderItem={item => (
                <Comment
                  key={item.commentId}
                  comment={item}
                  commentList={commentList}
                  setCommentList={setCommentList}
                  commentCount={commentCount}
                  setCommentCount={setCommentCount}
                />)}
            >
            </List>

          </div>
          : <div className='mt-4 mb-4' style={{ textAlign: 'center' }}>
            <p>No comment yet. Do you want to write something ?</p>
          </div>
      }

      <div className='custom-search mt-2'>
        <input
          id={post.id}
          type='text'
          className='custom-search-input shadow-none'
          placeholder='Write a comment...'
        >
        </input>
        <button
          className='custom-search-button shadow-none'
          type='submit'
          onClick={() => addNewComment()}
        >
          <FontAwesomeIcon icon={faPaperPlane} />
        </button>
      </div>

    </div >
  );
}

export default CommentList;

