import './App.css';
import Container from 'react-bootstrap/Container';
import Nav from 'react-bootstrap/Nav';
import Navbar from 'react-bootstrap/Navbar';
import bg from './img/bg.png';
import {createContext, lazy, useEffect, useState} from "react";
import data from './data';
import {Card} from "./components/components";
import {Route, Routes, useNavigate} from "react-router-dom";
import Detail from "./pages/Detail";
import Cart from "./pages/Cart";
import axios from "axios";
import {useQuery} from "@tanstack/react-query";



function checkedDuplicate(res, shoes) {
	let filter = res.data.filter(e => !shoes.map(e => e.id).includes(e.id));
	return [...shoes, ...filter];
}

// 리엑트 라이브러리
export let Context1 = createContext();

function App() {

	// vl필요할떄
	const Detail = lazy(() => import('./pages/Detail.js'));

	const [shoes, setShoes] = useState(data);
	const [재고] = useState([10, 11, 12]);
	const [showCnt, setShowCnt] = useState(0);
	const [addBtn, hideAddBtn] = useState(false);
	let navigate = useNavigate();

	useEffect(() => {
		if (!localStorage.getItem("watched")) {
			localStorage.setItem("watched", JSON.stringify([]));
		}
	}, []);

	// axios.get('https://codingapple1.github.io/userdata.json')
	// 		.then(res => res.data)
	let result = useQuery(['작명'], () =>
		axios.get('https://codingapple1.github.io/userdata.json')
				.then(res => res.data)
	);
	// console.log(result.data, result.isLoading);

	return (
			<div className="App">
				<Navbar bg="light" variant="light">
					<Container>
						<Navbar.Brand href="#home">ShoeShop</Navbar.Brand>
						<Nav className="me-auto">
							<Nav.Link onClick={() => navigate('/')}>Home</Nav.Link>
							<Nav.Link onClick={() => navigate('/cart')}>Cart</Nav.Link>
						</Nav>
						<Nav className={'ms-auto'}>
							{ result.isLoading && '로딩중' }
							{ result.isError && '에러남' }
							{ result.data && result.data.name }
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
						<Context1.Provider value={{재고, shoes}}>
							<Detail shoes={shoes}></Detail>
						</Context1.Provider>
					}></Route>

					<Route path="/cart" element={
						<Cart></Cart>
					}></Route>

					<Route path="/*" element={<div>에러페이지</div>}></Route>
				</Routes>


			</div>
	);
}


export default App;
