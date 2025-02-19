import {lusitana} from "@/app/ui/fonts";
import EventSorcererLogo from "@/app/ui/event-sorcerer-logo";
import Image from "next/image";
import SignIn from "@/app/ui/auth/SignIn";


export default function Page() {
    return (
        <main className="flex min-h-screen flex-col p-6">
            <div className="flex h-20 shrink-0 items-end rounded-lg bg-blue-500 p-4 md:h-52">
                <EventSorcererLogo/>
            </div>
            <div className="mt-4 flex grow flex-col gap-4 md:flex-row">
                <div className="flex flex-col justify-center gap-6 rounded-lg bg-gray-50 px-6 py-10 md:w-2/5 md:px-20">
                    <p className={`${lusitana.className} text-xl text-gray-800 md:text-3xl md:leading-normal`}>
                        Welcome in the Realm of the <br/><strong>Event Sorcerer</strong> with
                        the <strong>Keycloak</strong>!
                    </p>
                    <SignIn/>
                </div>
                <div className="flex items-center justify-center p-6 md:w-3/5 md:px-28 md:py-12">
                    <Image
                        src="/event-sorcerer.png"
                        width={1314}
                        layout={"responsive"}
                        height={1314}
                        className="hidden md:block"
                        alt="Screenshots of the dashboard project showing desktop version"
                    />
                </div>
            </div>
        </main>
    );
}
