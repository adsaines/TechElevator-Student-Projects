import Vue from 'vue'
import Router from 'vue-router'
import Home from './views/Home.vue'
import Login from './views/Login.vue'
import Register from './views/Register.vue'
import Courses from './views/Courses.vue'
import Leagues from './views/Leagues.vue'
import MyScores from './views/MyScores.vue'
import auth from './auth'
import LeagueDetail from './views/LeagueDetail.vue'

Vue.use(Router)

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: Home
    },
    {
      path: '/register',
      name: 'register',
      component: Register
    },
    {
      path: '/login',
      name: 'login',
      component: Login
    },
    {
    path: '/courses',
    name: 'courses',
    component: Courses
    },
    {
      path: '/leagues',
      name: 'leagues',
      component: Leagues
    },
    {
      path: '/myScores',
      name: 'myScores',
      component: MyScores
    },
    {
      path: '/leagueDetails',
      name: 'LeagueDetail',
      component: LeagueDetail
    }
  ]
});

router.beforeEach((to, from, next) => {
  // redirect to login page if not logged in and trying to access a restricted page
  const publicPages = ['/login', '/register', '/', '/courses', '/leagues'];
  const authRequired = !publicPages.includes(to.path);
  const loggedIn = auth.getUser();

  if (authRequired && !loggedIn) {
    return next('/login');
  }

  next();
});

export default router;
