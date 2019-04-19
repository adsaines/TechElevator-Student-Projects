
<!-- 
Shows your leagues
-->

<template>
    <div id="myLeagues">
        <div>
            <h6 v-if="leagueList.length == 0">You're not part of a league! Get some friends or create your own league!</h6>
            <table id="myLeaguesTable">
                <tr>
                    <th>League</th>
                    <th>Course</th>
                    <th></th>
                </tr>
                <tr v-for="league in leagueList" v-bind:key="league.leagueId">
                    <td>{{ league.name }}</td>
                    <td>{{ league.courseName }}</td>
                    <td>
                        <button type="submit" class="btn btn-md btn-primary" v-on:click="goToLeagueDetails(league)">
                            View League Details
                        </button>
                    </td>
                </tr>
            </table>
        </div>
        <button class="btn btn-primary" @click.prevent="showCreateLeague = !showCreateLeague">Create New League</button>
        <div v-if="showCreateLeague === true" class="createLeaguePopUp">
            <div  id="createLeague">
                <input class="form-control" type=text v-model="leagueName" placeholder="League Name">
                <select class="form-control" type=text v-model="selectedCourse" placeholder="Course Name">
                    <option v-for="course in golfCourses" :key="course.id" v-bind:value="{course}">{{ course.courseName }}</option>
                </select>
                <button type="submit" class="btn btn-primary" v-on:click="submitLeague()">Submit</button>
            </div>
            <div v-if="showCreated==true">
                <h3>League Created!</h3>
            </div>
        </div>
        <div id="user-invitations" class="container">
            <UserInvitations v-on:refresh-leagues="getUserLeagues()"/>
        </div>
    </div>
</template>

<script>
import auth from '../auth';
import UserInvitations from '@/Components/ViewUserInvitations';

export default {
    components: {
        UserInvitations
    },
    data() {
        return {
            leagueList: [],
            selectedCourse: [],
            leagueName: '',
            showCreateLeague: false,
            golfCourses: [],
            showCreated: false,
        }
    },
    methods: {
        goToLeagueDetails(league) {
            this.$router.push({name: 'LeagueDetail', params: {leagueInfo: league}});
        },
        getUserLeagues() {
        fetch(`${process.env.VUE_APP_REMOTE_API}/league/myLeagues`, {
            method: 'GET',
            headers: {Authorization: 'Bearer '+ auth.getToken()}
        })
        .then((response) => {
            if (response.ok) {
                return response.json();
            } 
        })
        .then((response) => {
            this.leagueList = response;
        })
        .catch((err) => console.error(err));
      },
      submitLeague() {
          const leagueInfo = {'name': this.leagueName, 'courseName': this.selectedCourse.course.courseName};
          console.table(leagueInfo);
          fetch(`${process.env.VUE_APP_REMOTE_API}/league/createLeague`, {
              method: 'POST',
                    headers: {
                        Authorization: 'Bearer '+ auth.getToken(),
                        Accept: 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(leagueInfo)
          })
          .then((response) => {
              if (response.ok) {
                this.showCreated= true;
                this.leagueName = '';
              }
          })
          .catch((err) => console.error(err));
      }
    },
     created() {
        this.getUserLeagues();
    },
    beforeMount() {
        fetch(`${process.env.VUE_APP_REMOTE_API}/course/getAll`, {
				method: 'GET',
				headers: {Authorization: 'Bearer '+ auth.getToken()}
            })
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                this.golfCourses = data
            })
			.catch((err) => console.error(err));
    }
}
</script>

<style>
#myLeagues {
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

.createLeaguePopUp {
    width: 50%;
    height: 100%;
    padding: 15px;
    margin: auto;
    margin-top: 4%;
    text-align: center;
    color: white;
    background-color: rgba(5, 5, 5, 0.671);
    border-radius: 10px;
}

.createLeaguePopUp .form-control {
    width: 30%;
    margin: auto;
    margin-bottom: 2%;
}

#myLeaguesTable {
    width: 60%;
}

#myLeaguesTable .btn {
    margin: 2%;
}

#myLeaguesTable th {
font-size: 1.75rem;
}

</style>
