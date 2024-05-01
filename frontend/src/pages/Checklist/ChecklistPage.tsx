// import MyCheckList from "../../components/Checklist/myChecklist";
// import ChecklistByDay from "../../components/Checklist/checklistByDay";
import HeaderBar from "../../components/HeaderBar/HeaderBar";
import {
    Tab,
    initTWE,
  } from "tw-elements";
  
  export default function ChecklistPage() {
      // 투어 아이디 불러오기
      const address: string[] = window.location.href.split('/');
      const tourId: string = address[address.length - 2];
      
      initTWE({ Tab });

    
    return (
        <>
            <header>
                <HeaderBar/>
            </header>
            <div>
                <div className="h-[20vh] bg-gray-100">
                    여행헤더 영역
                </div>
                <div className="h-[20vh] bg-blue-100">
                    기본정보 영역
                </div>
                <div>
                    <ul
                    className="mb-5 flex list-none flex-row flex-wrap border-b-0 ps-0"
                    role="tablist"
                    data-twe-nav-ref>
                    <li role="presentation">
                        <a
                        href="#tabs-home"
                        className="my-2 block border-x-0 border-b-2 border-t-0 border-transparent px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight text-neutral-500 hover:isolate hover:border-transparent hover:bg-neutral-100 focus:isolate focus:border-transparent data-[twe-nav-active]:border-primary data-[twe-nav-active]:text-primary dark:text-white/50 dark:hover:bg-neutral-700/60 dark:data-[twe-nav-active]:text-primary"
                        data-twe-toggle="pill"
                        data-twe-target="#tabs-home"
                        data-twe-nav-active
                        role="tab"
                        aria-controls="tabs-home"
                        aria-selected="true"
                        >Home</a>
                    </li>
                    <li role="presentation">
                        <a
                        href="#tabs-profile"
                        className="my-2 block border-x-0 border-b-2 border-t-0 border-transparent px-7 pb-3.5 pt-4 text-xs font-medium uppercase leading-tight text-neutral-500 hover:isolate hover:border-transparent hover:bg-neutral-100 focus:isolate focus:border-transparent data-[twe-nav-active]:border-primary data-[twe-nav-active]:text-primary dark:text-white/50 dark:hover:bg-neutral-700/60 dark:data-[twe-nav-active]:text-primary"
                        data-twe-toggle="pill"
                        data-twe-target="#tabs-profile"
                        role="tab"
                        aria-controls="tabs-profile"
                        aria-selected="false"
                        >Profile</a>
                    </li>
                    </ul>

                    <div className="mb-6">
                    <div
                        className="hidden opacity-100 transition-opacity duration-150 ease-linear data-[twe-tab-active]:block"
                        id="tabs-home"
                        role="tabpanel"
                        aria-labelledby="tabs-home-tab"
                        data-twe-tab-active>
                        Tab 1 content
                    </div>
                    <div
                        className="hidden opacity-0 transition-opacity duration-150 ease-linear data-[twe-tab-active]:block"
                        id="tabs-profile"
                        role="tabpanel"
                        aria-labelledby="tabs-profile-tab">
                        Tab 2 content
                    </div>
                    
                    </div>

                    {/* <MyCheckList/>
                    <ChecklistByDay/> */}
                </div>
            </div>
        </>
    );
}
