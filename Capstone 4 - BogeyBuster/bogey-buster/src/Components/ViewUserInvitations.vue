<template>
    <div id="viewUserInvitations">
        <h3>User Invitations</h3>
        <div id="invitation-container" class="container">
            <div>
                <!-- conditional messages -->
                <h6 v-if="userInvitations.length == 0">No invitations at this time.</h6>
                <h6 v-if="accepted == true">Invitation Accepted</h6>
                <h6 v-if="declined == true">Invitation Declined</h6>
                <h6 v-if="errors.length != 0">
                    <ul v-for="error in errors" v-bind:key="error"><li>{{error}}</li></ul>
                </h6>
            </div>
            <div class="alert alert-primary" v-for="invite in userInvitations" v-bind:key="invite.invitationId">
                <!-- includes the inviters (league organizers) name and the name of the league that you are being invited to -->
                <h6>The players of {{ invite.leagueName }} want you to join them.</h6>
                <button @click="addUserToLeague(invite)" type="button" class="btn btn-outline-success" >Accept</button>
                <button @click="removeInvitation(invite)" type="button" class="btn btn-outline-danger" >Decline</button>
            </div>
        </div>
    </div>
</template>

<script>
// import { setInterval } from 'timers';
import auth from '../auth';
export default {
    data() { return {
        userInvitations: [],
        accepted: false,
        declined: false,
        // timer: '',
        errors: []
    }},
    methods: {
        addUserToLeague(invite){
            // passes the userId and the leagueId back to the DB for addition to the league_members_and_scores table
            // calls remove invitation after successfully adding the user to the DB
            fetch(
                `${process.env.VUE_APP_REMOTE_API}/league/acceptLeagueInvitation`,
                {
                    method: 'POST',
                    headers: {
                        Authorization: 'Bearer '+ auth.getToken(),
                        Accept: 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(invite)
                }
            )
            .then( (response) => {
                if (response.ok) {
                    response.json();
                }
            })
            .then( (requestResult) => {
                // if there are errors returned then display them
                // set the accepted message to true or false
                if (requestResult.errors.length == 0){
                    this.accepted = true;
                } else {
                    this.errors = requestResult.errors;
                    this.accepted = false;
                }
                // refresh the user list & the league list
                this.getUserInvitations();
                this.$emit('refresh-leagues');
            })
            .catch((error) => (console.log(error)));

        },
        removeInvitation(invite){
            // removes the invitation from the DB
            fetch(
                `${process.env.VUE_APP_REMOTE_API}/league/declineLeagueInvitation`,
                {
                    method: 'POST',
                    headers: {
                        Authorization: 'Bearer '+ auth.getToken(),
                        Accept: 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(invite)
                }
            )
            .then( (response) => {
                if (response.ok) {
                    this.declined = true;
                } else {
                    this.declined = false;
                }
                return response.json();
            })
            .catch((error) => (console.log(error)));
        },
        getUserInvitations(){
            // sets the userInvitations array
            fetch(
                `${process.env.VUE_APP_REMOTE_API}/league/getUserInvitations`,
                {
                    method: 'GET',
                    headers: {Authorization: 'Bearer '+ auth.getToken()}
                }
            )
            .then ( (response) => { if (response.ok) { return response.json(); }})
            .then ( (invitationList) => { 
                this.userInvitations.push(invitationList);
            })
            .catch((error) => (console.log(error)))
        },
        setIndicators() {
            this.declined = false;
            this.accepted = false;
        }
    },
    created() {
        // initialize the users invitations and the user object when the page loads
        fetch(
            `${process.env.VUE_APP_REMOTE_API}/league/getUserInvitations`,
            {
                method: 'GET',
                headers: {Authorization: 'Bearer '+ auth.getToken()}
            }
        )
        .then ( (response) => { if (response.ok) { return response.json(); }})
        .then ( (invitationList) => { 
            this.userInvitations.push(invitationList);
        })
        .catch((error) => (console.log(error)))
        // this.timer = setInterval(this.setIndicators(), 60000);
    }
}
</script>

<style>
#viewUserInvitations {
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
