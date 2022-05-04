import React from 'react';
import { Link } from 'react-router-dom';
import axios from 'axios';
import default_avatar from '../../assets/default_avatar.png';

import Helper from '../../utils/Helper';

const Bookmark = ({
  bookmark,
  bookmarks,
  setBookmarks
}) => {
  const currentUser = JSON.parse(localStorage.getItem('currentUser'));

  // console.log('bookmark: ', bookmark);

  const removeBookmark = async () => {

    try {
      const removeBookmark = await
        axios.delete(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/bookmarks/author/${currentUser.id}/post/${bookmark.id}`, {
          headers: {
            Authorization: `Bearer ${currentUser.token}`
          }
        })

      console.log('removed bookmark: ', removeBookmark.data);

      const newBookmarkList = bookmarks.filter(ele => ele.id !== removeBookmark.data.postId);

      setBookmarks(newBookmarkList);
    }
    catch (err) {
      console.log('error delete bookmark');
    }

  }

  return (
    <div className='bookmark'>
      <div className='bookmark-text'>
        <div className='bookmark-avatar'>
          <img src={bookmark.avatarUrl ? bookmark.avatarUrl : default_avatar} alt="" />
        </div>
        <div className='bookmark-header'>

          <Link className='link' to={`/postdetail/${bookmark.id}`}>
            <span className='bookmark-title'>{bookmark.title}</span>
          </Link>
          <span className='bookmark-info'>
            <Link className='link' to={`/about/${bookmark.author}`}>
              {bookmark.author}
            </Link>
            {/* <FontAwesomeIcon className='dot-icon' icon={faCircle} size='1x'/> */}
            {/* <LineOutlined className='line' /> */}
            <span className='line'>•</span>
            {Helper.formatCreatedDate2(bookmark.createdDate)}
          </span>
        </div>
      </div>

      <div className='bookmark-remove'>
        <button className='remove-bookmark-button' onClick={() => removeBookmark()}>
          Remove
        </button>
      </div>
    </div>
  )
}

export default Bookmark;

