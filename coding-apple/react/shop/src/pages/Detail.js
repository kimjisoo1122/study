import {useParams} from "react-router-dom";

function Detail(props) {
  let {id} = useParams();
  console.log(id);
  let find = props.shoes.find(e => e.id == id);
  console.log(find);
  return (
      <div className="container">
        <div className="row">
          <div className="col-md-6">
            <img src={`https://codingapple1.github.io/shop/shoes${find.id + 1}.jpg`} width="100%"/>
          </div>
          <div className="col-md-6">
            <h4 className="pt-5">{props.shoes[id].title}</h4>
            <p>{find.content}</p>
            <p>{find.price} 원</p>
            <button className="btn btn-danger">주문하기</button>
          </div>
        </div>
      </div>
  )
}

// 상세페이지 url파라미터는 상세아이템의 고유id여야 한다.
export default Detail;