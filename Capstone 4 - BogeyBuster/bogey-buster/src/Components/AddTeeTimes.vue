<template>
    <div>
        <div>
        <form @submit.prevent="addTeeTime" id="addTeeTimeForm">
            <div class="alert alert-danger" role="alert" v-if="addTeeTimeErrors">
            There were problems creating this tee time.
            </div>
            <h1>Create a Tee Time</h1>
          <!--  <input v-model="query" :data="username"/> -->
            <input class="form-control teeTimeAdd" placeholder="Select a Username" v-model="selectedPerson">
            <input class="form-control teeTimeAdd" type="text" id="teeTime" placeholder="Enter a Tee Time" required v-model="teeTime">
            <button class="btn btn-primary">Submit Tee Time</button>
        </form>
        </div>

   <!-- <div class="alert alert primary" id="addTeeTimes">
            <h1> Add A Tee Time </h1>
            <vue-bootstrap-typeahead v-model="query" :data="golfCourses"/>
            <input class="form-control" placeholder="Select A Tee Time"/>
            <h1> Add A User To A Tee Time </h1>
            <vue-bootstrap-typeahead v-model="query" :data="username"/>
            <input class="form-control" placeholder="Invite"/>
            <button class="btn btn-primary">Submit</button>
        </div>
        <div class="alert alert primary" id="teeTimeInvite"> 
            <h1> Add A User To A Tee Time </h1>
            <vue-bootstrap-typeahead v-model="query" placeholder="Search For A Golfer Using Their Username" :data="golfCourses"/>
            <input class="form-control" placeholder="Invite"/>
            <button class="btn btn-primary">Add User To Tee Time</button>
    </div>-->
    </div>
</template>

<script> //tee time id, coursename, username

import VueBootstrapTypeahead from 'vue-bootstrap-typeahead'
import auth from '../auth';

export default {
    props: {
        leagueInfo: Object
    },
    componets: {
        VueBootstrapTypeahead
    },
    data() {
        return {
            username: [],
            query: '',
            teeTime: '',
            courseId: '',
            addTeeTimeErrors: false,
            selectedPerson: ''

        }
    },
    methods: {
        addTeeTime() {
            const teeTimeDetails={'teeTime' : this.teeTime, 'courseId' : this.leagueInfo.courseId, 'username' : this.selectedPerson}
            fetch(`${process.env.VUE_APP_REMOTE_API}/teeTime/addTeeTime`, {
                method: 'POST',
                headers: {
                Authorization: 'Bearer '+ auth.getToken(),
                Accept: 'application/json',
                'Content-Type': 'application/json'
                },
                body: JSON.stringify(teeTimeDetails),
            })
            .then((response) => {
                if (response.ok) {
                    this.$router.push({ path: '/leagues' });
                } else {
                    this.addTeeTimeErrors = true;
                }
            })
            .then((err) => console.error(err));
        }
    } 
}

</script>

<style>
#addTeeTimeForm {
    width: 100%;
    height: 100%;
    max-width: 800px;
    padding: 15px;
    margin: auto;
    margin-top: 3%;
    text-align: center;
    color: white;
    background-color: rgba(5, 5, 5, 0.671);
    border-radius: 10px;
}

.teeTimeAdd {
    width: 30%;
    display: inline-block;
    margin: 5px;
}
</style>