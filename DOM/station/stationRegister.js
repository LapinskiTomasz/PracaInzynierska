const vm = new Vue({
  el: '#app',
  data: {
    registerMessage:""
  },
  mounted () {
    this.registerMessage = "";
  },
  methods: {
    registerStation(name,login,password,street,homeNumber,city,zipCode,code){
      axios.post('http://localhost:8081/sapi/register',{name:name, login:login,password:password,street:street,homeNumber:homeNumber,city:city,zipCode:zipCode,code:code}).then((response) =>{
        console.log(response.data);
        if(response.data.code == 200){
          localStorage.setItem('RegisteredStationLogin', login);
          window.location.href = 'stationLogin.html';
        }
        if(response.data.code == 403){
          this.registerMessage = "Błędny kod rejestracyjny";
        }
        if(response.data.code == 409){
          this.registerMessage = "Taki login już istnieje";
        }

      }).catch( error => { console.log(error); });
    },

  }
});
