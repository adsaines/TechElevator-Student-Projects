<template>
    <div id="whole">
        <h1>Log Scores from match</h1>
        <h1>{{leagueInfo.leagueName}}</h1>
        <h4>Only the best scores will kept for the leaderboard.</h4>
        <!-- should only show up when errors occured in submitting or getting the scores from the backend -->
        <div id="error-box" v-if="errorsToDisplay.length != 0">
            <ul class="list-group">
                <li class="error-message list-group-item list-group-item-danger" v-for="error in errorsToDisplay" v-bind:key="error"> {{error}} </li>
            </ul>
        </div>
        <div class="container">
            <div>
                <table id="user-scores-table">
                    <tr>
                        <th scope="col">Username</th>
                        <th scope="col">Best Score / Strokes</th>
                        <th scope="col">Score From New Match</th>
                    </tr>
                    <tr v-for="user in leagueScores" v-bind:key="user.userId" class="user-information-row">
                        <td class="userName">{{user.userName}}</td>
                        <td class="bestScore">{{user.score}}</td>
                        <td class="newScore">
                            <input type="number" v-model="user.newScore"/>
                        </td>
                    </tr>
                    <!-- should only show up when there are no players in a League -->
                    <tr v-if="leagueScores.length === 0">
                        <td colspan="4"><h4>There are no players currently in this League.</h4></td>
                    </tr>
                </table>
                <!-- submit whole table at once -->
                <button type="submit" class="btn btn-primary" @click="submitScores">Submit Score</button>
            </div>
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
    data() {return {
        leagueScores: [],
        errorsToDisplay: []
    }},
    methods: {
        // 1: a fetch (post) that inputs the newly entered score(s) into the league_members_and_scores table, then updates the currentLeagueScores array with the new information
        submitScores(){
            fetch(
                `${process.env.VUE_APP_REMOTE_API}/league/submitLeagueScores/${this.leagueInfo.leagueId}`,
                {
                    method: 'POST',
                    headers: {
                        Authorization: 'Bearer '+ auth.getToken(),
                        Accept: 'application/json',
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify(this.leagueScores)
                }
            )
            .then ((response) => { return response.json(); })
            .then ( (requestResult) => {
                if (requestResult.errors.length === 0){
                    this.getScores();
                    this.errorsToDisplay = [];
                } else {
                    // go throughg the RequestResult.error property and fill up the errorsToDisplay array with any errors provided
                    this.errorsToDisplay = requestResult.errors;
                }
                // $emit('updateLeaderboard', true);
            })
            .catch((error) => (this.errorsToDisplay.push(error)));
        },
        // 2: a fetch (get) that initializes the currentLeagueScores array with information to be placed on the page
        getScores() {
            fetch(
                `${process.env.VUE_APP_REMOTE_API}/league/getLeagueScores/${this.leagueInfo.leagueId}`,
                {
                    method: 'GET',
                    headers: {
                        Authorization: 'Bearer '+ auth.getToken(),
                    }
                }
            )
            .then ( (response) => {
                if (response.ok){
                    this.errorsToDisplay = [];
                    return response.json();
                } 
            })
            .then((scoreData)=>{
                this.leagueScores=scoreData;
                this.leagueScores.map(currentPerson => currentPerson.newScore = 0);
            })
            .catch((error) => (console.log(error)))
        }
    },
    created() {
        this.getScores();
    }
}
</script>

<style>
#user-scores-table {
	background-color: black;
	color: white;
	border-radius: 10px;
    width: 100%;
	margin: auto;
	margin-bottom: 2%;
}

#whole {
    background-color: rgba(5, 5, 5, 0.671);
	border-radius: 10px;
    padding-bottom: 2%;
}
</style>
