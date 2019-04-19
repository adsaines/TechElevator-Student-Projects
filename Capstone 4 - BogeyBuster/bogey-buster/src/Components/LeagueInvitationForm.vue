<template>
    <div id="leagueSendInvites">
        <h1>Send League Invitations</h1>
         <div id="userSearch">
             <!-- Searches for users whose username or password are similar to the input search string -->
            <input class="form-control" v-model="searchString" @keyup="getUsers" placeholder="Enter Search Term"/>
            <button class="btn btn-secondary" @click="clearSearchResults">Clear Search Results</button>
        </div>
        <div v-if="invitationSent === true" class="alert alert-success">
            <!-- only shows up if the invitation was sent successfully -->
            <h6>Invitation Sent!</h6>
        </div>
        <div v-if="errorsToDisplay.length != 0" id="error-box" class="alert alert-danger">
            <!-- only shows up if errors occured during the invitation process -->
            <p v-for="error in errorsToDisplay" v-bind:key="error">{{error}}</p>
        </div>
        <div v-for="user in matchingUsers" v-bind:key="user.userId" class="alert alert-primary">
            <!-- display username and submit the userId when the invite is sent -->
            <h6>{{user.username}}</h6>
            <button @click="inviteUserToLeague(user.userId)" type="button" class="btn btn-outline-success" >Invite User</button>
        </div>
    </div>
</template>

<script>
// import so that we can send JWT token as authenticator
import auth from '../auth';

// general usage items for the page
export default {
    props: {
        leagueInfo: Object
    },
    data() { return {
        searchString: '',
        matchingUsers: [],
        invitationSent: false,
        errorsToDisplay: [],
        index: 'j'
    }},
    methods: {
        clearSearchResults() {
            this.matchingUsers = [];
        },
        getUsers() {
            // get the users that match the searchString for either email or username
            if (this.searchString != '') {
                this.errorsToDisplay = [];
                fetch(
                    `${process.env.VUE_APP_REMOTE_API}/user/getUsersForInvitations/${this.searchString}`,
                    {
                        method: 'GET',
                        headers: {Authorization: 'Bearer '+ auth.getToken()}
                    }
                )
                .then ( (response) => {
                    if (response.ok){
                        return response.json();
                    } 
                })
                .then((users)=>{
                    this.matchingUsers=users;
                })
                .catch((error) => (this.errorsToDisplay.push(error)))
            }
        },
        inviteUserToLeague(userId) {
            // take the leagueId and userId and pass them all back to java to create an invitation in the system
            const invitation = {'leagueId': this.leagueInfo.leagueId, 'invitee': userId};
            fetch(
                `${process.env.VUE_APP_REMOTE_API}/league/inviteUser`,
                {
                    method: 'POST',
                    headers: {
                        Authorization: 'Bearer '+ auth.getToken(),
                        Accept: 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(invitation)
                }
            )
            .then ((response) => {return response.json()})
            .then ((requestResult) => {
                if (requestResult.success === true){
                    this.invitationSent = true;
                    this.errorsToDisplay = [];
                } else {
                    // go through the RequestResult.error property and fill up the errorsToDisplay array with any errors provided
                    this.errorsToDisplay = requestResult.error;
                    this.invitationSent = false;
                }
            })
            .catch((error) => (this.errorsToDisplay.push(error)));
        }
    }
}
</script>

<style>
#leagueSendInvites {
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
