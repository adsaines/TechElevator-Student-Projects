<template>
    <div>
        <div id="addScores">
            <h1>Add New Score</h1>
            <vue-bootstrap-typeahead id="courseChoice" v-model="query" placeholder="Search For A Golf Course" :data="golfCourses"/>
            <input id="scoreInput" class="form-control" placeholder="Enter Score"/>
            <button class="btn btn-primary">Add Score</button>
        </div>
        <div id="oldScores">
            <h1>These are my old scores</h1>
        </div>
    </div>
</template>

<script>

import VueBootstrapTypeahead from 'vue-bootstrap-typeahead'
import auth from '../auth';

export default {
    components: {
        VueBootstrapTypeahead
    },
    data() {
    return {
      golfCourses: [],
      query: '',
      score: 0
    }
    },
    methods: {

    },
		created() {
			fetch(`${process.env.VUE_APP_REMOTE_API}/course/getAll`, {
				method: 'GET',
				headers: {Authorization: 'Bearer '+ auth.getToken()}
            })
            .then((response) => {
                return response.json();
            })
            .then((data) => {
                this.golfCourses = data.map(function (obj) {
                    return obj.courseName
                });
            })
			.catch((err) => console.error(err));
		}
}
</script>

<style>
#addScores {
    width: 50%;
    padding: 15px;
    margin: auto;
    margin-top: 4%;
    text-align: center;
    color: white;
    background-color: rgba(5, 5, 5, 0.671);
    border-radius: 10px;
    color: white;
}

#oldScores {
    width: 50%;
    padding: 15px;
    margin: auto;
    margin-top: 4%;
    text-align: center;
    color: white;
    background-color: rgba(5, 5, 5, 0.671);
    border-radius: 10px;
    color: white;
    
}

#scoreInput {
    width: 25%;
    display: inline-block;
}

#courseChoice {
    width: 72%;
    display: inline-block;
    margin-right: 2%;
    margin-bottom: 2%;
}

::placeholder { /* Chrome/Opera/Safari */
  color: black !important;
}

</style>
