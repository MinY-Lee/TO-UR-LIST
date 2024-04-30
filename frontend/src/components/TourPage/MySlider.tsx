import Slider from "react-slick";

export default function MySlider({ countryInfoList }) {
  var settings = {
    dots: true,
    infinite: true,
    speed: 500,
    slidesToShow: 1,
    slidesToScroll: 1,
    arrows: false,
  };

  return (
    <Slider {...settings}>
      {countryInfoList.length > 0 && countryInfoList.map((countryInfo, index) => (
          <div key={index} className="border-2 border-blue-200 rounded-2xl p-3">
              <div className="grid grid-cols-3">
                  <div className="flex flex-col items-center">
                      <div className="text-gray-600">언어</div>
                      <div className="text-center whitespace-pre">{ countryInfo.language.split(',').join('\n') }</div>
                  </div>
                  <div className="flex flex-col items-center">
                      <div className="text-gray-600">전압</div>
                      <div>{ countryInfo.voltage.split(',').join('\n') }</div>
                  </div>
                  <div className="flex flex-col items-center">
                      <div className="text-gray-600">시차(한국기준)</div>
                      <div>{ countryInfo.KST }시간</div>
                  </div>
              </div>
              <div>
                <div className="flex flex-col items-center">
                    <div className="text-gray-600">기후</div>
                    <div className="text-wrap">{ countryInfo.climate }</div>
                </div>
              </div>
            <div>
        </div>
    </div>
        
    ))}
  </Slider>
  );
}