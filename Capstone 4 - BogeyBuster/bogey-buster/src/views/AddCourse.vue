<template>
    <div>
        <form @submit.prevent="addCourse(); clearCourseForm();" id="addCourseForm">
            <div class="alert alert-danger" role="alert" v-if="addCourseErrors">
            <p v-for="error in errorResponse" v-bind:key="error">{{error}}</p>
            </div>
            <h1>Add New Course</h1>
            <input class="form-control courseAdd" type="text" id="courseName" placeholder="Course Name" required v-model="course.courseName">
            <input class="form-control courseAdd" type="text" id="city" placeholder="City" required v-model="course.city">
            <!-- <input class="form-control courseAdd" type="text" id="state" placeholder="State" required v-model="course.state"> -->
              <select class="form-control courseAdd" type="text" id="state" placeholder="State" required v-model="course.state">
                <option value="Alabama">Alabama</option>
                <option value="Alaska">Alaska</option>
                <option value="Arizona">Arizona</option>
                <option value="Arkansas">Arkansas</option>
                <option value="California">California</option>
                <option value="Colorado">Colorado</option>
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
            <input class="form-control courseAdd" type="number" id="zip" placeholder="Zip Code" required v-model="course.zipCode" min="10000" max="99999">
            <input class="form-control courseAdd" type="number" id="par" placeholder="Par" required v-model="course.par">
            <input class="form-control courseAdd" type="time" id="courseOpen" placeholder="Course Opening Time" required v-model="course.openTime">
            <input class="form-control courseAdd" type="text" id="courseClose" placeholder="Closing Time" required v-model="course.closeTime">
            <button class="btn btn-primary">Add Course</button>
        </form>
    </div>
</template>

<script>

import auth from '../auth';
import lists from '../Lists';

export default {
  data() { return {
    course: {
      courseName: '',
      city: '',
      state: '',
      zipCode: '',
      par: '',
      openTime: '',
      closeTime: '',
    },
    addCourseErrors: false,
    errorResponse: [],
    // stateList: lists.fullNameStateOptions()
  }},
  methods: {
    addCourse() {
      fetch(`${process.env.VUE_APP_REMOTE_API}/course/add`, {
        method: 'POST',
        headers: {
          Authorization: 'Bearer '+ auth.getToken(),
          Accept: 'application/json',
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(this.course),
      })
        .then((response) => {
          if (response.ok) {
              return response.json();
        }})
        .then((addCourseResponse) => {
            this.errorResponse = addCourseResponse.errors
            if (this.errorResponse.length === 0) {
            this.$router.push({ path: '/courses', query: { courseAdded: 'success' } });
          } else {
            this.addCourseErrors = true;
          }
        })

        .then((err) => console.error(err));
    },
    clearCourseForm() {
        this.course.courseName = '';
        this.course.city = '';
        this.course.state = '';
        this.course.zipCode = '';
        this.course.par = '';
        this.course.openTime = '';
        this.course.closeTime = '';
        this.addCourseErrors = false;
    }
  }
}
</script>

<style>
#addCourseForm {
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

input {
    margin: 2%;
}

.courseAdd {
    width: 30%;
    display: inline-block;
    margin: 5px;
}

 ::placeholder {
  font-size: 12px;
}

</style>
