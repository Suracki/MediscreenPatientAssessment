<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://github.com/Suracki/MediscreenPatientAssessment">
    <img src="/logomin.png" alt="Logo">
  </a>

<h3 align="center">Mediscreen: Patient Assessment</h3>

  <p align="center">
    Mediscreen Patient Assessment application.
    <br>
    Provides functionality for generating risk assessments for patients.

  </p>
</div>



<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#about-the-project">About The Project</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>
    <li><a href="#getting-started">Getting Started</a></li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>



<!-- ABOUT THE PROJECT -->
## About The Project

This is the Patient Assessment microservice for the Mediscreen application package.
<br><br>
This part of the application generates risk assessments for patients using data obtained from the other Mediscreen microservices.
<br><br>
In addition to the font end pages, the application also provides REST API endpoints.
<p align="right">(<a href="#top">back to top</a>)</p>



### Built With

* [Java 11](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Maven](https://maven.apache.org/)
* [Gson](https://github.com/google/gson)
* [RetroFit](https://square.github.io/retrofit/)
* [Thymeleaf](https://www.thymeleaf.org/)
* [Bootstrap](https://getbootstrap.com)


<p align="right">(<a href="#top">back to top</a>)</p>



<!-- GETTING STARTED -->
## Getting Started

This microservice is intended to be used as part of the Mediscreen package, and will not operate correctly without the other parts.
<br>Please refer to the Mediscreen Package repository for details on running the package as a whole.

<p align="right">(<a href="#top">back to top</a>)</p>



<!-- USAGE EXAMPLES -->
## Usage

### Front End

The application serves a Front End UI using Thymeleaf and Bootstrap, which can be accessed via the following URL:

/assessment/view/{id} -> Generate & View assessment for a specific patient<br>

### API

The application provides a REST API with the following endpoint

/assessment/api/get/{id} -> Generate & Get Assessment for a specific patient<br>

_For full details of API usage, please refer to the [API specification document](/REST%20API%20Specification.pdf)_

<p align="right">(<a href="#top">back to top</a>)</p>


<!-- CONTACT -->
## Contact

Simon Linford - simon.linford@gmail.com

Project Link: [https://github.com/Suracki/MediscreenPatientAssessment](https://github.com/Suracki/MediscreenPatientAssessment)

<p align="right">(<a href="#top">back to top</a>)</p>
