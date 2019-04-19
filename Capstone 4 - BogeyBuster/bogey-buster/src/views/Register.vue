<template>
    <div>
      <div>
      <form @submit.prevent="register">
          <div class="form-group">
            <img src="@/assets/bogeyBusterWhiteLogo.png" alt width="200" height="200" />
            <div class="alert alert-danger" role="alert" v-if="registrationErrors">
            <p v-for="error in errorResponse" v-bind:key="error">{{error}}</p>
            </div>
            <label for="forSpace6"></label>
            <input class="form-control" id="userName" aria-describedby="emailHelp" placeholder="User Name" required v-model="user.username">
            <label for="forSpace1"></label>
            <input class="form-control" id="firstName" aria-describedby="emailHelp" placeholder="First Name" required v-model="user.firstName">
            <label for="forSpace2"></label>
            <input class="form-control" id="lastName" aria-describedby="emailHelp" placeholder="Last Name" required v-model="user.lastName">
            <label for="forSpace3"></label>
            <input type="email" class="form-control" id="email" aria-describedby="emailHelp" placeholder="Email Address" required v-model="user.email">
            <small id="emailHelp" class="form-text">We'll never share your email with anyone else.</small>
            <label for="forSpace4"></label>
            <input type="password" class="form-control" id="exampleInputPassword1" placeholder="Password" required v-model="user.password">
            <label for="forSpace5"></label>
            <input type="password" class="form-control" id="exampleInputPassword2" placeholder="Re-enter Your Password" required v-model="user.confirmPassword">
          </div>
        <router-link :to="{ name: 'login' }">
              Have an account?
            </router-link>
        <button type="submit" class="btn btn-lg btn-primary btn-block">Create Account</button>
      </form>
    </div>
    </div>
</template>

<script>
export default {
name: 'register',
  data() {
    return {
      user: {
        firstName: '',
        lastName: '',
        email: '',
        username: '',
        password: '',
        confirmPassword: '',
        role: 'Golfer',
      },
      registrationErrors: false,
      errorResponse: [],
    };
  },
  methods: {
    register() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/register`, {
        method: 'POST',
        headers: {
          Accept: 'application/json',
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(this.user),
      })
        .then((response) => {
          if (response.ok) {
            return response.json();
        }})
        .then((registrationResponse) => {
            this.errorResponse = registrationResponse.errors
            if (this.errorResponse.length === 0) {
              this.$router.push({ name: 'login', params: { registration: true } });
            } else {
              this.registrationErrors = true;
            }
        })

        .then((err) => console.error(err));
    },
  },
};
</script>

<style>
body{
    background-image: url("https://i.postimg.cc/vBMj66CY/argyle1.jpg")
}

form {
  width: 100%;
  height: 100%;
  max-width: 330px;
  padding: 15px;
  margin: auto;
  margin-top: 4%;
  margin-bottom: 4%;
  text-align: center;
  background-color: rgba(5, 5, 5, 0.671);
  border-radius: 10px;
}

#emailHelp {
  color: white;
}
</style>
