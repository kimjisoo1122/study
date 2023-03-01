function Card(props) {
	return (
			<div className="col-md-4">
				{/*<img src={process.env.PUBLIC_URL + '/logo192.png'} alt="" style={{width:'80%'}}/>*/}
				<img src={`https://codingapple1.github.io/shop/shoes${props.shoes.id + 1}.jpg`} alt="" style={{width:'80%'}}/>
				<h4>{props.shoes.title}</h4>
				<p>{props.shoes.price}원</p>
			</div>
	);
}

export {Card};