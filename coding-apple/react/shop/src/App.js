import './App.css';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import bg from './img/bg.png';
import {useState} from "react";
import data from './data';
import {Card} from "./components/components";
import {Route, Routes, useNavigate} from "react-router-dom";
import Detail from "./pages/Detail";
import axios from "axios";


function checkedDuplicate(res, shoes) {
	let filter = res.data.filter(e => !shoes.map(e => e.id).includes(e.id));
	return [...shoes, ...filter];
}

function App() {

	const [shoes, setShoes] = useState(data);
	const [showCnt, setShowCnt] = useState(0);
	const [addBtn, hideAddBtn] = useState(false);
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
						<>
							<div className="container">
								<div className="row">
									{
										shoes.map((e, i) => <Card shoes={e} key={i}></Card>)
									}
								</div>
							</div>
							{
								addBtn
										?
										null
										:
										<button onClick={() => {
											if (showCnt === 0) {
												axios.get('https://codingapple1.github.io/shop/data2.json')
														.then(res => {
															let items = checkedDuplicate(res, shoes);
															setShoes(items);
															setShowCnt(showCnt + 1);
														})
														.catch(error => {
															console.log(error);
														});
											} else if (showCnt === 1) {
												axios.get('https://codingapple1.github.io/shop/data3.json')
														.then(res => {
															let items = checkedDuplicate(res, shoes);
															setShoes(items);
														})
												hideAddBtn(true);
											}
										}
										}>상품더보기</button>
							}

						</>
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
