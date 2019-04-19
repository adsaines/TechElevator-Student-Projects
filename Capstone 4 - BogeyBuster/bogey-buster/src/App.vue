<template>
  <div id="app">

    <title>Golfing - Community | Bogey Buster</title>
    
    <div id="topnav">
      <router-link to="/">Home</router-link>
      <!-- <router-link v-if="isLoggedIn" to="/MyScores">My Scores</router-link> -->
      <router-link v-if="isLoggedIn" to="/courses">Golf Courses</router-link>
      <router-link v-if="isLoggedIn" to="/leagues">View Leagues</router-link>
      <p v-if="isLoggedIn" class="right" id="username">Welcome, {{currentUser.sub}}</p>
      
       <router-link v-if="!isLoggedIn" class="right" to="/login">Login</router-link>
      <a v-if="isLoggedIn" class="right" href="/" @click="logout">Logout</a>
      <router-link v-if="!isLoggedIn" class="right" to="/register">Register</router-link>
    </div>

    <router-view @login="updateUser"/>
  </div>
</template>

<script>

import auth from './auth';

export default {
  data() {
    return {
      currentUser: auth.getUser(),
    }
  },
  methods: {
    logout() {
      auth.logout();
      currentUser = null;
      this.$router.go('/');
    },
    updateUser() {
      this.currentUser = auth.getUser();
    }
  },
  computed: {
    isLoggedIn() {
      return this.currentUser != null
    }
  }
};
</script>

<style>
#topnav {
  overflow: hidden;
  background-color: black;
}

#topnav a {
  float: left;
  color: #f2f2f2;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 17px;
  height: 70px;
}

#topnav a:hover {
  background-color: #ddd;
  color: black;
}

#topnav .right {
  float: right;
}

#username {
  color: #f2f2f2;
  text-align: center;
  padding: 14px 16px;
  text-decoration: none;
  font-size: 20px;
  line-height: 26px;
  font-weight: bold;
}
</style>
