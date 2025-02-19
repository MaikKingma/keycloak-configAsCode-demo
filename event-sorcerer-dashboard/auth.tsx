import NextAuth from "next-auth"
import authConfig from "./auth.config"
import PostgresAdapter from "@auth/pg-adapter";
import {connectionPool} from "@/db";

export const { handlers, auth, signIn, signOut } = NextAuth({
    adapter: PostgresAdapter(connectionPool),
    session: { strategy: "jwt" },
    ...authConfig,
})