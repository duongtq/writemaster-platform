import React from 'react';
import { Switch, Route, Link, useRouteMatch } from 'react-router-dom';

import { Layout, Menu } from 'antd';
import Profile from './Profile';
import Notification from './Notification';
import BookmarkList from './bookmark/BookmarkList';
import Login from './Login';
import SocialPage from './social/SocialPage';
import CreatePostForm from './post/CreatePost';

import {
  UserOutlined,
  RetweetOutlined,
  NotificationOutlined,
  TeamOutlined,
  EditOutlined,
  CommentOutlined,
} from '@ant-design/icons';
import ChatPage from './chat/ChatPage';

const { Content, Sider } = Layout;

const UserPage = ({
  action,
  setAction,
  postList,
  setPostList }) => {

  let { path, url } = useRouteMatch();

  const currentUser = JSON.parse(localStorage.getItem('currentUser'));

  if (!currentUser) {
    return (
      <Login
        action={action}
        setAction={setAction}
      />
    )
  }

  return (
    // <Router>

    <Layout>
      <Sider
        style={{
          overflow: 'auto',
          height: '700px',
          position: 'absolute',
          // left: 50,

        }}
      >
        <div className="logo" />
        <Menu theme="dark" mode="inline" defaultSelectedKeys={['1']}>
          <Menu.Item key="1" icon={<UserOutlined />}>
            <Link to={`${url}/profile`}>
              Profile
            </Link>
          </Menu.Item>
          <Menu.Item key='2' icon={<EditOutlined />}>
            <Link to={`${url}/create-post`}>
              Write a post
            </Link>
          </Menu.Item>
          {/* <Menu.Item key="3" icon={<CommentOutlined />}>
            <Link to={`${url}/conversations`}>
              Conversations
            </Link>
          </Menu.Item> */}
          <Menu.Item key="4" icon={<NotificationOutlined />}>
            <Link to={`${url}/notifications`}>
              Notifications
            </Link>
          </Menu.Item>

          <Menu.Item key="5" icon={<RetweetOutlined />}>
            <Link to={`${url}/bookmarks`}>
              Reading List
            </Link>
          </Menu.Item>
          {/*
          <Menu.Item key="6" icon={<TeamOutlined />}>
            <Link to='/social'>
              Social corner
            </Link>
          </Menu.Item> 
          */}
        </Menu>
      </Sider>
      <Layout className="site-layout" style={{ marginLeft: 200 }}>
        <Content style={{ overflowY: 'auto', borderRight: '0.5px solid #bbbfbf' }}>

          <div className="site-layout-background" style={{ padding: 12, height: '700px' }}>

            <Switch>
              <Route path={`${url}/profile`}>
                <Profile
                  action={action}
                  setAction={setAction}
                />
              </Route>
              <Route path={`${url}/notifications`}>
                <Notification />
              </Route>
              <Route path={`${url}/bookmarks`}>
                <BookmarkList />
              </Route>


              {/* <Route exact path="/profile">
                  <Redirect to="/userPage" />
                </Route> */}

              <Route path='/social'>
                <SocialPage />
              </Route>

              <Route path={`${url}/create-post`}>
                <CreatePostForm
                  // history={history}
                  postList={postList}
                  setPostList={setPostList}
                />
              </Route>

              <Route path={`${url}/conversations`}>
                <ChatPage />
              </Route>

              <Route path={`${url}/userPage`}>
                <Profile
                  action={action}
                  setAction={setAction}
                />
              </Route>
            </Switch>

          </div>
        </Content>

      </Layout>

    </Layout>

  )
}

export default UserPage;
