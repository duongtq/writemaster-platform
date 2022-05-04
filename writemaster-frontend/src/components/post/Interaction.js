import React from 'react';
import { useState, useEffect } from 'react';
import CommentList from '../CommentList';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faHeart } from '@fortawesome/free-regular-svg-icons';
import { faCommentAlt } from '@fortawesome/free-regular-svg-icons';
import { faHeart as faHeartLiked } from '@fortawesome/free-solid-svg-icons';
// import { faShareAlt } from '@fortawesome/free-solid-svg-icons';
import { faBookmark } from '@fortawesome/free-regular-svg-icons';
import { faBookmark as faBookmarked } from '@fortawesome/free-solid-svg-icons';
import { faSlidersH } from '@fortawesome/free-solid-svg-icons';
import { Link } from 'react-router-dom'
import { faEdit } from '@fortawesome/free-solid-svg-icons'
import { faTrash } from '@fortawesome/free-solid-svg-icons'
import { faFlag } from '@fortawesome/free-solid-svg-icons'
import { Menu, Dropdown, Button, Modal } from 'antd';
import { Radio, Space } from 'antd';
import { Input } from 'antd';

import axios from 'axios';

const { TextArea } = Input;

const Interaction = ({ post, postList, setPostList }) => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));

  const authorId = currentUser.id;
  const postId = post.id;
  const token = currentUser.token;

  // console.log('Current user: ', currentUser);

  const [likeCount, setLikeCount] = useState(0);
  const [commentCount, setCommentCount] = useState(0);
  const [bookmarkCount, setBookmarkCount] = useState(0);

  // const [likedByCurrentUser, setLikedByCurrentUser] = useState(false);
  const [currentPost, setCurrentPost] = useState(post);

  // const [displayCommentList, setDisplayCommentList] = useState(false);

  const likeItem = post.likes.find(ele => ele.authorId === currentUser.id);

  const bookmarkItem = post.bookmarks.find(ele => ele.authorId === currentUser.id);

  const [authorLikePost, setAuthorLikePost] = useState(likeItem ? likeItem.liked : false)

  // const [currentLike, setCurrentLike] = useState({});
  const [authorBookmarkPost, setAuthorBookmarkPost] = useState(bookmarkItem === null);

  let currentLikeItem;
  let currentBookmarkItem;

  if (post.likes.length !== 0) {
    const XXX = post.likes.find(ele => ele.postId === postId && ele.authorId === authorId);

    if (XXX) {
      currentLikeItem = XXX;
    } else {
      currentLikeItem = {
        liked: false
      };
    }
  } else {
    currentLikeItem = {
      liked: false
    }
  }

  if (post.bookmarks.length !== 0) {
    const bookmarkedOrNot = post.bookmarks.find(ele => ele.postId === postId && ele.authorId === authorId);

    if (bookmarkedOrNot) {
      currentBookmarkItem = {
        bookmarked: true
      };
    } else {
      currentBookmarkItem = {
        bookmarked: false
      };
    }
  } else {
    currentBookmarkItem = {
      bookmarked: false
    }
  }

  // const currentLikeItem = currentPost.likes.find(ele => ele.postId === postId && ele.authorId === authorId);

  // console.log('currentLikeItem: ', currentLikeItem);
  // console.log('Interaction: post like ', post.likes);
  // console.log(`Author id ${currentUser.id} like post id ${post.id}: ${currentLikeItem.liked}`);

  const removeAntDesignEffect = () => {
    const body = document.getElementsByTagName("body")[0];
    body.classList.remove("ant-scrolling-effect");
    body.style.removeProperty('width');
    // body.style.setProperty('width', 'calc(100% + 15px)');
    // body.style.overflowX = 'hidden';
    body.style.overflowY = 'scroll';
  }

  useEffect(() => {
    axios.get(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/id/${post.id}`, {
      headers: {
        Authorization: `Bearer ${currentUser.token}`
      }
    })
      .then(res => {
        console.log('current post: ', res.data);
        setCurrentPost(res.data);
        updatePost();
      })
      .catch(err => {
        console.log('error get post');
      })

    setLikeCount(currentPost.numberOfLikes);
    setCommentCount(currentPost.numberOfComments);
    setBookmarkCount(currentPost.numberOfBookmarks);

    // setComments(post.comments);
  }, []);

  const menu = (
    <Menu
      style={{
        width: '150px'
      }}
    >
      {currentUser.id === post.authorId
        ? <><Menu.Item
          key='1'
        >
          <Link
            to={`/postedit/${post.id}`}>
            <FontAwesomeIcon icon={faEdit} /><span className='ml-2'>Edit</span>
          </Link>
        </Menu.Item>
          <Menu.Item
            key='2'
          >

            <div onClick={() => showModal()}>
              <FontAwesomeIcon icon={faTrash} />
              <span className='ml-2' >&nbsp;Delete</span>
            </div>

          </Menu.Item></>
        : <Menu.Item
          key='1'
        >
          <div onClick={() => showReportModal()}>

            <FontAwesomeIcon icon={faFlag} /><span className='ml-2'>Report</span>
          </div>

        </Menu.Item>
      }
    </Menu>
  );

  const [deletePostModal, setDeletePostModal] = useState(false);

  const showModal = () => {
    setTimeout(() => {
      removeAntDesignEffect();
    }, 0);
    setDeletePostModal(true);
  };

  const handleOk = async () => {
    try {
      const deletePostResult = await axios.delete(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/${post.id}`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });

      console.log('delete post result: ', deletePostResult);

    } catch (err) {
      console.log(err);
    }

    try {
      const getAllPost = await axios.get(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });
      setPostList(getAllPost.data);
      localStorage.setItem('postList', JSON.stringify(getAllPost.data));
    } catch (err) {
      console.log(err);
    }

    setDeletePostModal(false);
  };

  const handleCancel = () => {
    setDeletePostModal(false);
  };


  const updatePost = async () => {
    try {
      const res = await axios
        .get(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/posts/id/${postId}`, {
          headers: {
            Authorization: `Bearer ${currentUser.token}`
          }
        });
      // console.log('Updated post with like number ', res.data);
      setLikeCount(res.data.numberOfLikes);

      setBookmarkCount(res.data.numberOfBookmarks);

      setCurrentPost(res.data);

      // console.log('postList ', postList);
      // const tempPostList = postList.filter(ele => ele.id !== postId);

      // console.log('tempPostList ', tempPostList);

      const res1 = await axios.get(`http://localhost:8080/api/v1/posts`, {
        headers: {
          Authorization: `Bearer ${currentUser.token}`
        }
      });

      // console.log('res1.data: ', res1.data);

      setPostList(res1.data);

      // console.log('Post updated with new liked');

    } catch (err) {
      console.log('Something wrong');
    }
  }

  // updatePost();

  const toggleLike = () => {
    setAuthorLikePost(!authorLikePost);

    // setTimeout(() => {
    if (!authorLikePost && currentLikeItem.liked === false) {
      // send like to server
      // console.log('GOT CHA: ', currentUser.token);
      axios.post(`http://localhost:8080/api/v1/likes/author/${authorId}/post/${postId}`, null, {
        headers: {
          "Authorization": `Bearer ${token}`
        }
      }).then(res => {
        console.log('like res: ', res.data);

        // setLikeCount(currentPost.numberOfLikes - 1);

        updatePost();
      }).catch(err => {
        console.log('Error like post: ', err);
      })
    } else {
      axios.delete(`http://localhost:8080/api/v1/likes/author/${authorId}/post/${postId}`, {
        headers: {
          "Authorization": `Bearer ${token}`
        }
      }).then(res => {
        console.log('delete res: ', res.data);
        // setLikeCount(res.data.likes);

        updatePost();
      }).catch(err => {
        console.log('Error unlike post: ', err);
      })
    }
  }

  const toggleBookmark = () => {
    setAuthorBookmarkPost(!authorBookmarkPost);

    if (!authorBookmarkPost && currentBookmarkItem.bookmarked === false) {
      // send like to server
      // console.log('GOT CHA: ', currentUser.token);
      axios.post(`http://localhost:8080/api/v1/bookmarks/author/${authorId}/post/${postId}`, null, {
        headers: {
          "Authorization": `Bearer ${token}`
        }
      }).then(res => {
        console.log('bookmark result: ', res.data);

        // setLikeCount(currentPost.numberOfLikes - 1);

        updatePost();
      }).catch(err => {
        console.log('Error like post: ', err);
      })
    } else {
      axios.delete(`http://localhost:8080/api/v1/bookmarks/author/${authorId}/post/${postId}`, {
        headers: {
          "Authorization": `Bearer ${token}`
        }
      }).then(res => {
        console.log('unbookmark result: ', res.data);
        // setLikeCount(res.data.likes);

        updatePost();
      }).catch(err => {
        console.log('Error unbookmark post: ', err);
      })
    }
  }

  // TODO: report form modal

  const [reportContentModal, setReportContentModal] = useState(false);
  const [reportReason, setReportReason] = useState(0);
  const [errorVisible, setErrorVisible] = useState('hidden');

  const showReportModal = () => {
    setTimeout(() => {
      removeAntDesignEffect();
    }, 0);
    setReportContentModal(true);
  }

  const handleReportOk = () => {

    // TODO: send report here
    setReportContentModal(false);
  }

  const handleReportCancel = () => {
    setReportContentModal(false);
  }

  const onReasonChange = (e) => {
    console.log('radio checked: ', e.target.value);
    setReportReason(e.target.value);
  };

  return (
    <div className='mt-4 mb-4'>
      <Modal
        visible={deletePostModal}
        title="Delete post"
        onOk={handleOk}
        onCancel={handleCancel}
        footer={[
          <Button
            key="back"
            onClick={handleCancel}
            style={{
              width: '100px',
              height: '40px'
            }}>
            Cancel
          </Button>,
          <Button
            key='submit'
            type='primary'
            onClick={handleOk}
            style={{
              width: '100px',
              height: '40px'
            }}>
            Submit
          </Button>,
        ]}
      >
        <h4>{post.title}</h4>
        <p className="post-info">{post.info}</p>
        <p>{post.description}</p>
      </Modal>

      <Modal
        width='800px'
        visible={reportContentModal}
        title="Report content"
        onOk={handleReportOk}
        onCancel={handleReportCancel}
        footer={[
          <Button
            key="back"
            onClick={handleReportCancel}
            style={{
              width: '100px',
              height: '40px'
            }}>
            Cancel
          </Button>,
          <Button
            key='submit'
            type='primary'
            onClick={handleReportOk}
            style={{
              width: '100px',
              height: '40px'
            }}>
            Report
          </Button>,
        ]}
      >
        {/* <h4>{post.title}</h4>
        <p className="post-info">{post.info}</p>
        <p>{post.description}</p> */}
        <div>
          <span className='report-thank-you-text'>
            Thank you for reporting any abuse that violates our code of conduct or terms and conditions.<br />
            We continue to try to make this environment a great one for everybody.
          </span>

          <div className='mt-2 mb-2'>
            <Radio.Group
              onChange={(e) => onReasonChange(e)}
              value={reportReason}
            >
              <Space
                direction='vertical'
              >
                <Radio value={1}>Spam or copyright issue</Radio>
                <Radio value={2}>Harassment or hate speech</Radio>
                <Radio value={3}>Violence</Radio>
                <Radio value={4}>Misinformation</Radio>
                <Radio value={5}>Nudity</Radio>
              </Space>
            </Radio.Group>
          </div>

          {/* <div> */}
            <span>Note</span><br />
            <span>Please provide any additional information or context that
              will help us understand and handle the situation.</span><br />
            <TextArea
              showCount
              maxLength={200}
              style={{
                height: 100,
                marginTop: '10px'
              }}
            />
          {/* </div> */}

          <div style={{ visibility: `${errorVisible}`, marginTop: '2px', color: 'red', textAlign: 'center' }}>

            Your message should not be empty.
          </div>

        </div>

      </Modal>

      <hr className='mb-4' color='red' />

      <ul className='interaction-activity'>
        <li>
          <div>
            {currentLikeItem.liked
              ? <FontAwesomeIcon icon={faHeartLiked} size='2x' style={{ color: 'red' }} onClick={() => toggleLike()} />
              : <FontAwesomeIcon icon={faHeart} size='2x' onClick={() => toggleLike()} />
            }
            <span className='ml-2 noselect'>{likeCount}</span>
          </div>
        </li>
        <li>
          <div>
            <FontAwesomeIcon icon={faCommentAlt} size='2x' />
            <span className='ml-2 noselect'>{commentCount}</span>
          </div>
        </li>
        <li>
          <div>
            {
              currentBookmarkItem.bookmarked
                ? <FontAwesomeIcon icon={faBookmarked} size='2x' style={{ color: '#4F46E5' }} onClick={() => toggleBookmark()} />
                : <FontAwesomeIcon icon={faBookmark} size='2x' onClick={() => toggleBookmark()} />
            }
            {/* <FontAwesomeIcon icon={faBookmark} size='2x' /> */}
            <span className='ml-2 noselect'>{bookmarkCount}</span>
          </div>
        </li>
        <li>
          <div>
            <Dropdown
              overlay={menu}
              placement='topCenter'
              trigger={['click']}
              arrow
            >
              <FontAwesomeIcon icon={faSlidersH} size='2x' style={{ color: '#sad123' }} />
            </Dropdown>

          </div>
        </li>
      </ul>

      <hr className='mt-4' color='red' />

      <CommentList post={currentPost} commentCount={commentCount} setCommentCount={setCommentCount} />

    </div>
  );
}

export default Interaction;
