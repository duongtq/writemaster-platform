import React, { useState, useEffect, useRef } from 'react';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faEdit } from '@fortawesome/free-solid-svg-icons'
import { faTrash } from '@fortawesome/free-solid-svg-icons'
import { faFlag } from '@fortawesome/free-solid-svg-icons'
import { faEllipsisH } from '@fortawesome/free-solid-svg-icons'
import Helper from '../utils/Helper';
// import { useFormik } from 'formik';
// import * as Yup from 'yup';
import axios from 'axios';
// import $ from 'jquery';
import { Modal, Menu, Dropdown, Button } from 'antd';

import { Editor } from '@tinymce/tinymce-react';
import 'antd/dist/antd.css';


const Comment = ({ comment, commentList, setCommentList, commentCount, setCommentCount }) => {

  const currentUser = JSON.parse(localStorage.getItem('currentUser'));


  const removeAntDesignEffect = () => {
    const body = document.getElementsByTagName("body")[0];
    body.classList.remove("ant-scrolling-effect");
    body.style.removeProperty('width');
    // body.style.setProperty('width', 'calc(100% + 15px)');
    // body.style.overflowX = 'hidden';
    body.style.overflowY = 'scroll';
  }

  useEffect(() => {
    removeAntDesignEffect();
  });

  const menu = (
    <Menu
      style={{
        width: '150px'
      }}
    >
      {currentUser.id === comment.authorId
        ? <><Menu.Item
          key='1'
        >
          <div onClick={() => showEditModal()}>
            <FontAwesomeIcon icon={faEdit} /><span className='ml-2' >Edit</span>

          </div>
        </Menu.Item>
          <Menu.Item
            key='2'
          >

            <div onClick={() => showDeleteModal()}>
              <FontAwesomeIcon icon={faTrash} />
              <span className='ml-2' >&nbsp;Delete</span>
            </div>

          </Menu.Item></>
        : <Menu.Item
          key='1'
        >
          <div>
            <FontAwesomeIcon icon={faFlag} /><span className='ml-2'>Report</span>

          </div>
        </Menu.Item>
      }
    </Menu>
  );

  const [errorVisible, setErrorVisible] = useState('hidden');

  // console.log('currentUser: ', currentUser);
  const authorId = currentUser.id;

  const postId = comment.postId;
  const commentId = comment.commentId;

  const editorRef = useRef(null);
  // const log = () => {
  //   if (editorRef.current) {
  //     console.log(editorRef.current.getContent());
  //   }
  // };

  const createdOn = Helper.formatCreatedDate1(comment.createdOn);

  // EDIT COMMENT MODAL
  const [isEditModalVisible, setIsEditModalVisible] = useState(false);

  const showEditModal = () => {
    setIsEditModalVisible(true);
  };

  const showDeleteModal = () => {
    setIsDeleteModalVisible(true);
  }

  const handleEditOk = () => {
    // test
    // removeAntDesignEffect();

    if (!editorRef.current.getContent()) {
      setErrorVisible('visible');
      return;
    } else {
      setErrorVisible('hidden');
    }

    if (editorRef.current.plugins.wordcount.body.getWordCount() > 80) {
      const commentTooLong = Modal.error({
        title: 'Error',
        content: 'Long comment. Maximum length is 80 words.'
      });
      commentTooLong.update();
      return;
    }


    const updatedCommentPayload = {
      commentId: comment.commentId,
      content: editorRef.current.getContent(),
      lastEditedOn: new Date()
    }

    console.log('updated comment: ', updatedCommentPayload);

    axios.put(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/comments/author/${authorId}/post/${postId}`, updatedCommentPayload, {
      headers: {
        Authorization: `Bearer ${currentUser.token}`
      }
    }).then(res => {
      const tempCommentList = commentList.filter(ele => ele.commentId !== commentId);

      setCommentList([res.data, ...tempCommentList].sort((a, b) => {
        if (a.commentId > b.commentId) {
          return 1;
        }
        if (a.commentId < b.commentId) {
          return -1;
        }
        return 0;
      }));
    }).catch(err => {
      console.error('Error update comment');
    });

    setTimeout(() => {
      const modal = Modal.success({
        title: 'Your comment updated successfully',
      });
    }, 2);
    setIsEditModalVisible(false);

  };

  const handleEditCancel = () => {
    removeAntDesignEffect()

    setIsEditModalVisible(false);

    setErrorVisible('hidden');

    console.log('Comment to restore: ', comment.content);

    editorRef.current.setContent(comment.content);

  };

  // DELETE COMMENT MODAL
  const [isDeleteModalVisible, setIsDeleteModalVisible] = useState(false);

  const handleDeleteCancel = () => {
    setIsDeleteModalVisible(false);
  }

  const handleDeleteOk = () => {
    const deletedCommentPayload = {
      commentId: comment.commentId,
    }

    console.log('deleted comment payload: ', deletedCommentPayload);

    axios.delete(`${process.env.REACT_APP_API_SERVER_DOMAIN}:${process.env.REACT_APP_API_SERVER_PORT}/${process.env.REACT_APP_API_VERSION}/comments/author/${authorId}/post/${postId}`,
      {
        headers: {
          Authorization: `Bearer ${currentUser.token}`
        },
        data: deletedCommentPayload,
      }).then(res => {
        const tempCommentList = commentList.filter(ele => ele.commentId !== commentId);
        setCommentCount(commentCount - 1);
        setCommentList([...tempCommentList].sort((a, b) => {
          if (a.commentId > b.commentId) {
            return 1;
          }
          if (a.commentId < b.commentId) {
            return -1;
          }
          return 0;
        }));
      }).catch(err => {
        console.log('Error delete comment: ', err);
      });

    setTimeout(() => {
      const modal = Modal.success({
        title: 'Your comment deleted successfully',
      });
    }, 2);
    setIsDeleteModalVisible(false);
  }

  return (
    <div className='single-comment mt-2 mb-2'>
      <>
        <Modal
          className='delete-comment-modal'
          title='Delete comment'
          visible={isDeleteModalVisible}
          onOk={handleDeleteOk}
          onCancel={handleDeleteCancel}
          maskClosable={false}
          centered={true}
          width={600}
          footer={[
            <Button onClick={() => handleDeleteCancel()} style={{ width: '100px', height: '40px' }}>
              Cancel
            </Button>,
            <Button onClick={() => handleDeleteOk()} style={{ width: '100px', height: '40px' }}>
              Delete
            </Button>
          ]}
        >
          <div className='mt-2 mb-2' style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontSize: '20px' }}>{comment.author}</span>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
              <span style={{ fontSize: '11px', width: '220px' }}>Created: {createdOn}</span>
              {comment.lastEditedOn
                ? <span style={{ fontSize: '11px', width: '220px' }}>Last edited: {Helper.formatCreatedDate1(comment.lastEditedOn)}</span>
                : <span style={{ fontSize: '11px', width: '220px' }}>&nbsp;</span>}
            </div>
          </div>
          <div>
            <span>{comment.content}</span>
          </div>
        </Modal>
      </>

      <>
        <Modal
          className='edit-comment-modal'
          title='Edit comment'
          visible={isEditModalVisible}
          onOk={handleEditOk}
          onCancel={handleEditCancel}
          maskClosable={false}
          centered={true}
          width={600}
          footer={[
            <Button onClick={() => handleEditCancel()} style={{ width: '100px', height: '40px' }}>
              Cancel
            </Button>,
            <Button onClick={() => handleEditOk()} style={{ width: '100px', height: '40px' }}>
              Save
            </Button>
          ]}
        >

          <div className='mt-2 mb-2' style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
            <span style={{ fontSize: '20px' }}>{comment.author}</span>
            <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start' }}>
              <span style={{ fontSize: '11px', width: '220px' }}>Created: {createdOn}</span>
              {comment.lastEditedOn
                ? <span style={{ fontSize: '11px', width: '220px' }}>Last edited: {Helper.formatCreatedDate1(comment.lastEditedOn)}</span>
                : <span style={{ fontSize: '11px', width: '220px' }}>&nbsp;</span>}
            </div>
          </div>
          <>
            <Editor
              onInit={(evt, editor) => editorRef.current = editor}
              // id={`commentContent-${commentId}`}
              initialValue={comment.content}
              init={{
                height: 200,
                menubar: false,
                plugins: [
                  'advlist autolink lists link image charmap print preview anchor',
                  'searchreplace visualblocks code fullscreen',
                  'insertdatetime media table paste code help wordcount image media'
                ],
                toolbar: 'undo redo | formatselect | ' +
                  'bold italic backcolor | alignleft aligncenter ' +
                  'alignright alignjustify | bullist numlist outdent indent | ' +
                  'removeformat | wordcount | visualblocks | image | media | help',
                toolbar_mode: 'sliding',
                content_style: 'body { font-family:Helvetica,Arial,sans-serif; font-size:14px }',
              }
              }
            />
          </>
          {/* <div style={{ visibility: `${errorVisible}`, marginTop: '2px', color: 'red', textAlign: 'center' }}></div> */}
          <div style={{ visibility: `${errorVisible}`, marginTop: '2px', color: 'red', textAlign: 'center' }}>Comment should not be empty</div>
        </Modal>
      </>
      <article className='comment' >
        <div className='mb-2' style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center' }}>
          <div className='comment-header'>
            <img className='comment-author-avatar' src={comment.authorAvatarUrl} alt="" width='40px' height='40px' />
            <Link style={{ color: 'black', display: 'inline-block', marginLeft: '0.5rem' }} to={`/about/${comment.author}`}>
              <span style={{ fontSize: '20px', fontWeight: '500'}}>{comment.author}</span>
            </Link>
          </div>

          <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'flex-start', marginRight: '1.5em' }}>
            <span style={{ fontSize: '11px', width: '250px' }}>Created: {createdOn}</span>

            {comment.lastEditedOn
              ? <span style={{ fontSize: '11px', width: '250px' }}>Last edited: {Helper.formatCreatedDate1(comment.lastEditedOn)}</span>
              : <span style={{ fontSize: '11px', width: '250px' }}>&nbsp;</span>}
          </div>

        </div>

        <div style={{ display: 'flex', alignItems: 'center' }}>
          <div className='comment-content' dangerouslySetInnerHTML={{ __html: comment.content }}>
          </div>

          <div className='ml-2 mr-2'>

            <Dropdown
              overlay={menu}
              placement='bottomCenter'
              trigger={['click']}
              arrow
            >
              <button type='button' className='comment-action'>
                <FontAwesomeIcon icon={faEllipsisH} />
              </button>
            </Dropdown>
          </div>
        </div>
      </article>
    </div >
  )
}

export default Comment;

