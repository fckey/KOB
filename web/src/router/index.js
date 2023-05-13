import { createRouter, createWebHistory } from 'vue-router'

import PkIndexView from '../view/pk/PkIndexView';
import RecordIndexView from '../view/record/RecordIndexView';
import RecordContentView from '../view/record/RecordContentView';
import RankListIndexView from '../view/ranklist/RankListIndexView';
import UserBotIndexView from '../view/user/bot/UserBotIndexView';
import NotFoundView from '../view/error/NotFoundView';
import UserAccountLoginView from '../view/user/account/UserAccountLoginView';
import UserAccountRegisterView from '../view/user/account/UserAccountRegisterView';

import store from '../store/index'

const routes = [
    {
      path: "/",
      name: "home",
      redirect: '/pk/',
      meta:{
        requestAuth: true
      }
    },
    {
      path: "/pk/",
      name: "pk_index",
      component: PkIndexView,
      meta:{
        requestAuth: true
      }
    },
    {
      path: "/record/",
      name: "record_index",
      component: RecordIndexView,
      meta:{
        requestAuth: true
      }
    },
    {
      path: "/record/:recordId/",
      name: "record_content",
      component: RecordContentView,
      meta:{
        requestAuth: true
      }
    },
    {
      path: "/ranklist/",
      name: "ranklist_index",
      component: RankListIndexView,
      meta:{
        requestAuth: true
      }
    },
    {
      path: "/user/bot/",
      name: "user_bot_index",
      component: UserBotIndexView,
      meta:{
        requestAuth: true
      }
    },
    {
      path: "/user/account/login/",
      name: "user_account_login",
      component: UserAccountLoginView,
      meta:{
        requestAuth: false
      }
    },
    {
      path: "/user/account/register",
      name: "user_account_register",
      component: UserAccountRegisterView,
      meta:{
        requestAuth: false
      }
    },
    {
      path: "/404/",
      name: "404",
      component: NotFoundView,
    },
    {
      path: "/:catchAll(.*)",
      redirect: '/404/'
    }
]

const router = createRouter({
  history: createWebHistory(),
  routes
});

router.beforeEach((to, from, next) => {
  // 如果要去的页面需要授权，并且用户没有登录
  if(to.meta.requestAuth && !store.state.user.is_login){
    next({name: 'user_account_login'});
  }else{
    next();
  }
});


export default router