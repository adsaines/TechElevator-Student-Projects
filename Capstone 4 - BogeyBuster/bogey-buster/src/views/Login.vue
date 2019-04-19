<template>
    <div>
        <form @submit.prevent="login">
          <div class="form-group">
            <img src="@/assets/bogeyBusterWhiteLogo.png" alt width="200" height="200" />
            <div class="alert alert-danger" role="alert" v-if="invalidCredentials">
        Invalid username and password!
      </div>
            <label for="forSpace"></label>
            <input type="text" class="form-control" id="usernameInput" placeholder="Username" required autofocus v-model="user.username">
            <label for="forSpace"></label>
            <input type="password" class="form-control" id="passwordInput" placeholder="Password" required v-model="user.password">
          </div>
          <router-link :to="{ name: 'register' }">
                Need an account?
              </router-link>
          <button type="submit" class="btn btn-lg btn-primary btn-block">Login</button>
          <!-- <div>
            <h4 v-if="isRegistered==true">We decided to let you in!</h4>
          </div> -->
        </form>
    </div>
</template>

<script>
import auth from '../auth';
export default {
    name: 'login',
  components: {},
  data() {
    return {
      user: {
        username: '',
        password: '',
        isRegistered: false
      },
      invalidCredentials: false,
    };
  },
  methods: {
    login() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/login`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
      .then((response) => {
        if (response.ok) {
          return response.text();
        } else {
          this.invalidCredentials = true;
        }
      })
      .then((token) => {
        if (token != undefined) {
          if (token.includes('"')) {
            token = token.replace(/"/g, '');
          }
          auth.saveToken(token);
          this.$emit('login');
          this.$router.push('/');
        }
      })
      .catch((err) => console.error(err));
    },
  },
  // created() {
  //   if (this.$route.params.registration==true) {
  //     this.isRegistered = true;
  //   }
  // }
};
</script>


<style scoped>
form {
  width: 100%;
  height: 100%;
  max-width: 330px;
  padding: 15px;
  margin: auto;
  margin-top: 7%;
  text-align: center;
  color: white;
  background-color: rgba(5, 5, 5, 0.671);
  border-radius: 10px;
}

</style>
