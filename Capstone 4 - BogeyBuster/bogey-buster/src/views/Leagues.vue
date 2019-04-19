<template>
    <div>
       <MyLeagues v-if="isLoggedIn()"></MyLeagues>
    </div>
</template>

<script>

//import LogMatchScores from '../Components/LogMatchScores';
import auth from '../auth';
import MyLeagues from '@/views/MyLeagues';

export default {
    components: {
    MyLeagues
  },
  methods: {
    isLoggedIn() {
      return auth.getUser() != null
    }
  },
}

</script>

<style>
#allLeagues {
    width: 80%;
    height: 100%;
    padding: 15px;
    margin: auto;
    margin-top: 4%;
    text-align: center;
    color: white;
    background-color: rgba(5, 5, 5, 0.671);
    border-radius: 10px;
}
</style>
