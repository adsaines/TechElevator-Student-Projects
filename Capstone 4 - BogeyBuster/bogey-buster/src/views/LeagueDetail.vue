<!-- 
Shows the league leaderboard, invite players to league, and log scores option for admins
-->

<template>
    <div id="myLeagueDetails" v-bind:class="{ leagueOrganizer: userId === leagueInfo.organizerId, nonLeagueOrganizer: userId != leagueInfo.organizerId}">
        <div class="leaderboard">
            <h1>{{leagueInfo.name}} at {{ leagueInfo.courseName }} Leaderboard</h1>
            <table id="leagueLeaderboard">
                <tr>
                    <th>Rank</th>
                    <th>Player</th>
                    <th>Score</th>
                </tr>
                <tr v-for="(leagueScore, index) in leagueScores" v-bind:key="leagueScore.leagueId">
                    <td>{{ index + 1 }}</td>
                    <td>{{ leagueScore.userName }}</td>
                    <td>{{ leagueScore.score }}</td>
                </tr>
            </table>
        </div>

        <div v-if="userId === leagueInfo.organizerId" id="invite-form" class="container">
            <InvitationForm v-bind:leagueInfo="leagueInfo" />
        </div>
        <div v-if="userId === leagueInfo.organizerId" id="log-scores" class="container">
            <LogScores v-bind:leagueInfo="leagueInfo" />
            <!-- @updateLeaderboard="leaderboardUpdate()" -->
        </div>
        <div v-if="userId === leagueInfo.organizerId" id="add-tee-times" class="container" >
            <AddTeeTimes v-bind:leagueInfo="leagueInfo"/>
        </div>
        <div id="get-tee-times" class="container">
            <GetTeeTimes/>
        </div>
    </div>
</template>

<script>
import auth from '../auth';
import InvitationForm from '../Components/LeagueInvitationForm';
import LogScores from '../Components/LogMatchScores';
import AddTeeTimes from '@/Components/AddTeeTimes';
import GetTeeTimes from '@/Components/GetTeeTimes';

export default {
    props: {

    },
    components: {
        InvitationForm,
        LogScores,
        AddTeeTimes,
        GetTeeTimes,
    },

    data() {
        return {
            leagueScores: [],
            leagueInfo : this.$route.params.leagueInfo,
            userId: ''
        }
    },
    methods: {
        leaderboardUpdate() {
            console.log('Im running');
            fetch(`${process.env.VUE_APP_REMOTE_API}/league/getLeagueScores/${this.leagueInfo.leagueId}`, {
                method: 'GET',
				headers: {Authorization: 'Bearer '+ auth.getToken()}
			})
			.then((response) => {
				if (response.ok) {
					return response.json();
                } 
			})
			.then((scores) => {
				this.leagueScores = scores;
            })
            .catch((err) => console.error(err));
        }
    },
    created() {
            this.leaderboardUpdate();
        },
    beforeMount() {
            fetch(`${process.env.VUE_APP_REMOTE_API}/user/getUserId`, {
                method: 'GET',
                headers: {Authorization: 'Bearer '+ auth.getToken()}
            })
            .then((response) => {
				if (response.ok) {
					return response.json();
						} 
			})
            .then((response) => {
                this.userId = response;
            })
            .catch((err) => console.error(err));
        }
}
</script>

<style>
.leagueOrganizer {
    width: 80%;
	height: 100%;
	padding: 15px;
	margin: auto;
	margin-top: 4%;
	margin-bottom: 4%;
	text-align: center;
	color: white;
	background-color: rgba(5, 5, 5, 0.671);
	border-radius: 10px;
    display: grid;
	grid-template-columns: 50% 50%;
	grid-template-rows: auto;
	grid-column-gap: 10px;
    grid-row-gap: 10px;
	grid-template-areas: 
    "leaderboard invite"
    "leaderboard ."
    "leaderboard logScore"
    "getTeeTimes createTeeTimes";
}

.nonLeagueOrganizer {
    width: 80%;
	height: 100%;
	padding: 15px;
	margin: auto;
	margin-top: 4%;
	margin-bottom: 4%;
	text-align: center;
	color: white;
	background-color: rgba(5, 5, 5, 0.671);
	border-radius: 10px;
}

.leaderboard {
    grid-area: leaderboard;
	justify-self: center;
	align-self: center;
    width: 100%;
    height: 100%;
}

#invite-form {
    grid-area: invite;
	justify-self: center;
	align-self: center;
}

#log-scores {
    grid-area: logScore;
	justify-self: center;
	align-self: center;
    width: 100%;
}

#add-tee-times {
    grid-area: createTeeTimes;
	justify-self: center;
	align-self: center;
}

#get-tee-times {
    grid-area: getTeeTimes;
	justify-self: center;
	align-self: center;
}

table {
	background-color: black;
	color: white;
	width: 50%;
	border-radius: 10px;
	margin: auto;
	margin-bottom: 2%;
}

#leagueLeaderboard {
    width: 100%;
}

#leagueLeaderboard th {
    width: 33%;
}
</style>
