import './App.css';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import bg from './img/bg.png';
import {useState} from "react";
import data from './data';
import {Card} from "./components";


function App() {

  let [shoes, setShoes] = useState(data);

  return (
    <div className="App">
      <Navbar bg="light" variant="light">
        <Container>
          <Navbar.Brand href="#home">ShoeShop</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link href="#home">Home</Nav.Link>
            <Nav.Link href="#cart">Cart</Nav.Link>
          </Nav>
        </Container>
      </Navbar>

      <div className="main-bg" style={{backgroundImage: `url(${bg})`}}>
      </div>

      <div className="container">
        <div className="row">
          {
            shoes.map(e => <Card shoes={e}></Card>)
          }
        </div>
      </div>

    </div>
  );
}


export default App;
