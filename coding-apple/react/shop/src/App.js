import './App.module.css';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import bg from './img/bg.png';
import {useState} from "react";
import data from './data';
import {Card} from "./components/components";
import {Route, Routes, useNavigate} from "react-router-dom";
import Detail from "./pages/Detail";
import style from 'styled-components';


function App() {

  const [shoes, setShoes] = useState(data);
  let navigate = useNavigate();

  return (


    <div className="App">
      <Navbar bg="light" variant="light">
        <Container>
          <Navbar.Brand href="#home">ShoeShop</Navbar.Brand>
          <Nav className="me-auto">
            <Nav.Link onClick={() => navigate('/')}>Home</Nav.Link>
            <Nav.Link href="#cart">Cart</Nav.Link>
          </Nav>
        </Container>
      </Navbar>
      <div className="main-bg" style={{backgroundImage: `url(${bg})`}}>
      </div>

      <Routes>
        <Route path="/" element={
          <div className="container">
            <div className="row">
              {
                shoes.map((e, i)=> <Card shoes={e} key={i}></Card>)
              }
            </div>
          </div>
        }>
        </Route>
        <Route path="/detail/:id" element={
          <Detail shoes={shoes}></Detail>
        }></Route>

        <Route path="/*" element={<div>에러페이지</div>}></Route>
      </Routes>


    </div>
  );
}


export default App;
