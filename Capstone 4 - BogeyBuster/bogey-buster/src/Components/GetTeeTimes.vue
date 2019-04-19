<template>
    <div id="getTeeTimes">
        <div class="alert alert primary">
            <h1>Upcoming Tee Times</h1>
            <div>
                <table id="upcomingMatches">
                    <tr>
                        <th>Time</th>
                        <th>Course Name</th>
                    </tr>
                    <tr v-for="match in userMatches" v-bind:key="match.teeTime">
                        <td> {{match.teeTime.teeTime.dayOfWeek}}, {{match.teeTime.teeTime.month}}/{{match.teeTime.teeTime.dayOfMonth}}/{{match.teeTime.teeTime.year}} at {{match.teeTime.teeTime.hour}}:{{match.teeTime.teeTime.minute}}0 </td>
                        <td> {{match.courseName}}</td>
                    </tr>
                </table>
            </div>

         <!--   <ul>
                <li> tee times go here </li>
                <li v-for="player in match.players" v-bind:key="player.userId">
                    Username: {{player.username}}
                </li>
            </ul> -->
        </div>
    </div>
</template>

<script>
import auth from '../auth';

export default {
    data() {
        return {
            userMatches: []
        }
    },
    
    created() {
        fetch(`${process.env.VUE_APP_REMOTE_API}/teeTime/getMatchInfo`, {
            method: 'GET',
            headers: {Authorization: 'Bearer '+ auth.getToken()}
        })
        .then((response) => {
            return response.json();
        })
        .then((data) => {
            this.userMatches = data;
        })
        .catch((err) => console.error(err));
    }
}

</script>

<style>
#getTeeTimes {
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

#upcomingMatches {
    width: 100%;
}

#upcomingMatches th {
    width: 50%;
}
</style>
