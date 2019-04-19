<template>
    <div id="container">
		<div class="searchAndResults">
			<h1>Find golf courses in your state</h1>
			<div id="stateSearch">
					<form>
						<div>
							<select class="form-control" name="state" v-model="searchString" @change="searchByState">
								<option value="Alabama">Alabama</option>
								<option value="Alaska">Alaska</option>
								<option value="Arizona">Arizona</option>
								<option value="Arkansas">Arkansas</option>
								<option value="California">California</option>
								<option value="Colorado">Coloradok</option>
								<option value="Connecticut">Connecticut</option>
								<option value="Delaware">Delaware</option>
								<option value="Florida">Florida</option>
								<option value="Georgia">Georgia</option>
								<option value="Hawaii">Hawaii</option>
								<option value="Idaho">Idaho</option>
								<option value="Illinois">Illinois</option>
								<option value="Indiana">Indiana</option>
								<option value="Iowa">Iowa</option>
								<option value="Kansas">Kansas</option>
								<option value="Kentucky">Kentucky</option>
								<option value="Louisiana">Louisiana</option>
								<option value="Maine">Maine</option>
								<option value="Maryland">Maryland</option>
								<option value="Massachusetts">Massachusetts</option>
								<option value="Michigan">Michigan</option>
								<option value="Minnesota">Minnesota</option>
								<option value="Mississippi">Mississippi</option>
								<option value="Missouri">Missouri</option>
								<option value="Montana">Montana</option>
								<option value="Nebraska">Nebraska</option>
								<option value="Nevada">Nevada</option>
								<option value="New Hampshire">New Hampshire</option>
								<option value="New Jersey">New Jersey</option>
								<option value="New Mexico">New Mexico</option>
								<option value="New York">New York</option>
								<option value="North Carolina">North Carolina</option>
								<option value="North Dakota">North Dakota</option>
								<option value="Ohio">Ohio</option>
								<option value="Oklahoma">Oklahoma</option>
								<option value="Oregon">Oregon</option>
								<option value="Pennsylvania">Pennsylvania</option>
								<option value="Rhode Island">Rhode Island</option>
								<option value="South Carolina">South Carolina</option>
								<option value="South Dakota">South Dakota</option>
								<option value="Tennessee">Tennessee</option>
								<option value="Texas">Texas</option>
								<option value="Utah">Utah</option>
								<option value="Vermont">Vermont</option>
								<option value="Virginia">Virginia</option>
								<option value="Washington">Washington</option>
								<option value="West Virginia">West Virginia</option>
								<option value="Wisconsin">Wisconsin</option>
								<option value="Wyoming">Wyoming</option>
							</select>
						</div>
					</form>
			</div>
			<table v-if= "courseData.length > 0" class="table table-striped" > 
				<tr>
					<th>Name</th>
					<th>City</th>
					<th>State</th>
					<th>Zip</th>
					<th>Par</th>
					<th>Open Time</th>
					<th>Close Time</th>
				</tr>
				<tr v-for="course in courseData" v-bind:key="course.courseId">
					<td> {{course.courseName}}</td>
					<td> {{course.city}}</td>
					<td> {{course.state}}</td>
					<td> {{course.zipCode}}</td>
					<td> {{course.par}}</td>
					<td> {{course.openTime}}</td>
					<td> {{course.closeTime}}</td>
				</tr>
			</table>
		</div>
			<GoogleMap id="GoogleMap" :courseArray = "courseData" />
		<AddCourse class="addCourse" v-if="isAdmin()"/>
    </div>
</template>

<script>

import auth from '../auth';
import AddCourse from '@/views/AddCourse';
import GoogleMap from '@/Components/GoogleMap';


export default {
	components: {
		AddCourse,
		GoogleMap
	},
data() {
      return {
		courseData: [],
        selected: null,
        stateOptions: [],
		cityOptions: [],
		zipOptions: [],
		searchString: ''
			}
	},
	methods: {
		searchByState() {
			if (this.searchString === ''){
				return;
			}
			fetch(`${process.env.VUE_APP_REMOTE_API}/course/searchByState/${this.searchString}`, {
				method: 'GET',
				headers: {Authorization: 'Bearer '+ auth.getToken()}
			})
			.then((response) => {
				if (response.ok) {
					return response.json();
						} 
			})
			.then((response) => {
				this.courseData = response;
			})
			.catch((err) => console.error(err));
		},
		updateCourseData() {
			
		},
		searchByCity() {
			fetch(`${process.env.VUE_APP_REMOTE_API}/course/searchByCity/${this.selected}`, {
				method: 'GET',
				headers: {Authorization: 'Bearer '+ auth.getToken()}
			})
			.then((response) => {
				if (response.ok) {
					return response.json();
						} 
			})
			.then((response) => {
				this.courseData = response;
				this.$router.push(`/course/displayCourses`);
			})
			.catch((err) => console.error(err));
		},
		searchByZip() {
			fetch(`${process.env.VUE_APP_REMOTE_API}/course/searchByZip/${this.selected}`, {
				method: 'GET',
				headers: {Authorization: 'Bearer '+ auth.getToken()}
			})
			.then((response) => {
				if (response.ok) {
					return response.json();
						} 
			})
			.then((response) => {
				this.courseData = response;
				this.$router.push(`/course/displayCourses`);
			})
			.catch((err) => console.error(err));
		},
		isLoggedIn() {
					return auth.getUser() != null
			},
			isAdmin() {
				return auth.getUser().rol === "Administrator";
			}
	},
}
</script>

<style scoped>

form {
    width: 75%;
}

#stateSearch {
	width: 80%;
	margin: auto;
	margin-top: 5%;
}

table {
	background-color: black;
	color: white;
	width: 50%;
	border-radius: 10px;
	margin: auto;
	margin-bottom: 2%;
}

#container {
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
	grid-template-columns: 1fr 2fr;
	grid-template-rows: auto;
	grid-column-gap: 10px;
	grid-template-areas: 
    "search map"
    "addCourse map";
}

.form-control {
	width: 140px;
	margin: auto;
	margin-bottom: 4%;
}

#GoogleMap {
		width: 100%;
		height: 100%;
		min-height: 600px;
		margin: auto;
		background-color: grey;
		grid-area: map;
 }

 .searchAndResults{
	grid-area: search;
 }
 
 .addCourse {
	grid-area: addCourse;
	justify-self: center;
	align-self: center;
 }

 #stateSearch .form-control {
	margin-bottom: 0;
 }

</style>
