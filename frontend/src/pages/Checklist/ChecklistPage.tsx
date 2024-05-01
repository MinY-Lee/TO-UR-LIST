import HeaderBar from "../../components/HeaderBar/HeaderBar";
import ChecklistTab from '../../components/Checklist/checklistTab'
  
export default function ChecklistPage() {
      // 투어 아이디 불러오기
      const address: string[] = window.location.href.split('/');
      const tourId: string = address[address.length - 2];

    
    return (
        <>
            <header>
                <HeaderBar/>
            </header>
            <div className="overflow-y-scroll">
                <div className="h-[20vh] bg-gray-100">
                    여행헤더 영역
                </div>
                <div className="h-[20vh] bg-blue-100">
                    기본정보 영역
                </div>
                <div>
                    <ChecklistTab/>
                    

                    
                </div>
            </div>
        </>
    );
}
