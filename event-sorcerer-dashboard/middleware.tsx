import NextAuth from "next-auth"
import authConfig from "./auth.config"

export const { auth: middleware } = NextAuth(authConfig)

// See "Matching Paths" below to learn more
export const config = {
    matcher: ['/dashboard', '/dashboard/:path*'],
}