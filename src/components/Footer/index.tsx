import GitHubIcon from "../../assets/icons/github.svg?react";

import { motion } from "framer-motion";

interface FooterProps {
  isOnInfoDisplay?: boolean;
}

const Footer: React.FC<FooterProps> = ({ isOnInfoDisplay }) => {
  return (
    <motion.footer
      initial={{ opacity: 0 }}
      whileInView={{ opacity: 1 }}
      transition={{ duration: 0.5 }}
      className={`flex gap-4 ${isOnInfoDisplay ? "text-sky-300" : "text-sky-700"
        } ${isOnInfoDisplay ? "dark:text-sky-300" : "dark:text-sky-600"} ${isOnInfoDisplay ? "" : "absolute bottom-8"
        } ${isOnInfoDisplay ? "flex-col items-center min-[380px]:flex-row" : ""}`}
      data-test={isOnInfoDisplay ? "info-footer" : "footer"}
    >
      <p className="flex gap-2 text-xs sm:text-sm self-end font-extralight">
        Developed by{" "}
        <a
          href="https://github.com/joELL-lab"
          target="_blank"
          rel="noreferrer noopener"
          aria-label="Link to joELL-lab profile"
          title="Link to joELL-lab profile"
          className={`transition-all underline underline-offset-2 hover:scale-105 hover:text-sky-200 ${isOnInfoDisplay ? "text-sky-400" : "text-sky-600"
            } ${isOnInfoDisplay ? "dark:text-sky-400" : "dark:text-sky-400"}`}
        >
          joELL-lab
        </a>{" "}
        @2026
      </p>
      <div className="flex items-center gap-4 min-[380px]:gap-2">
        <a
          href="https://github.com/joELL-lab"
          target="_blank"
          rel="noreferrer noopener"
          aria-label="Link to joELL-lab profile"
          title="Link to joELL-lab profile"
          className="flex items-center no-underline  transition-all hover:text-sky-200 hover:scale-105"
        >
          <GitHubIcon className="w-6 h-6" />
        </a>
      </div>
    </motion.footer>
  );
};

export default Footer;
