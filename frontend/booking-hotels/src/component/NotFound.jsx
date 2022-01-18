import React from 'react';

import styles from "../styles/notfound.module.css";

const NotFound = () => {
    return (
			<div className={styles.contenedor}>
				<h1 className={styles.title}>404 Not Found</h1>
				<h3 className={styles["msg"]}>
					Lo sentimos, la página que busca no existe 😔
				</h3>
				<div className="clase" />
				<div className="clase" />
			</div>
		);
};

export default NotFound;