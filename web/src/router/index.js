import { createRouter, createWebHistory } from 'vue-router'

import PkIndexView from '../view/pk/PkIndexView';
import RecordIndexView from '../view/record/RecordIndexView';
import RankListIndexView from '../view/ranklist/RankListIndexView';
import UserBotIndexView from '../view/user/bot/UserBotIndexView';
import NotFoundView from '../view/error/NotFoundView';
import UserAccountLoginView from '../view/user/account/UserAccountLoginView';
import UserAccountRegisterView from '../view/user/account/UserAccountRegisterView';

const routes = [
    {
      path: "/",
      name: "home",
      redirect: '/pk/'
    },
    {
      path: "/pk/",
      name: "pk_index",
      component: PkIndexView,
      meta:{
        title: '首页'
      }
    },
    {
      path: "/record/",
      name: "record_index",
      component: RecordIndexView,
    },
    {
      path: "/ranklist/",
      name: "ranklist_index",
      component: RankListIndexView,
    },
    {
      path: "/user/bot/",
      name: "user_bot_index",
      component: UserBotIndexView,
    },
    {
      path: "/user/account/login/",
      name: "user_account_login",
      component: UserAccountLoginView,
    },
    {
      path: "/user/account/register",
      name: "user_account_register",
      component: UserAccountRegisterView,
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
})


export default router