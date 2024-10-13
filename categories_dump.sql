--
-- PostgreSQL database dump
--

-- Dumped from database version 14.13 (Ubuntu 14.13-0ubuntu0.22.04.1)
-- Dumped by pg_dump version 14.13 (Ubuntu 14.13-0ubuntu0.22.04.1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: products_categories; Type: TABLE; Schema: public; Owner: postgres
--

-- CREATE TABLE public.products_categories (
--     category_id bigint NOT NULL,
--     product_id bigint NOT NULL
-- );
--
--
-- ALTER TABLE public.products_categories OWNER TO postgres;

--
-- Data for Name: products_categories; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.products_categories (category_id, product_id) FROM stdin;
1	1
1	2
1	3
1	23
1	28
1	135
1	136
1	144
1	145
1	58
1	212
2	4
2	5
2	16
2	137
2	188
2	189
2	191
2	208
2	209
2	213
2	217
3	6
3	11
3	205
3	15
3	24
3	25
3	34
3	40
3	41
3	49
3	152
3	153
3	154
3	155
3	156
3	157
3	158
3	159
3	160
4	75
4	76
4	77
4	89
4	90
4	91
4	92
4	93
4	94
4	95
4	96
4	97
4	98
4	99
4	100
4	101
4	127
4	206
4	164
4	165
4	166
4	167
4	168
4	169
4	170
4	171
4	172
4	173
4	174
4	175
4	176
4	177
4	178
4	179
4	180
4	181
4	182
5	114
5	115
5	116
5	117
5	118
5	118
5	119
5	120
5	121
5	122
5	123
5	124
5	125
5	126
5	128
5	129
5	130
5	131
5	132
5	133
5	134
5	190
1	19
3	18
3	88
4	220
1	27
3	10
2	21
3	9
2	12
1	13
2	38
1	79
1	80
2	14
3	20
2	22
1	29
2	39
3	85
1	149
1	142
1	187
3	8
1	145
4	222
4	223
3	15
1	44
1	43
4	84
4	82
4	87
3	7
4	109
1	45
4	163
1	150
4	161
4	162
3	207
2	215
1	210
4	111
4	112
1	193
1	218
2	32
3	53
1	71
4	83
4	108
4	110
4	46
\.


--
-- Name: products_categories category_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

-- ALTER TABLE ONLY public.products_categories
--     ADD CONSTRAINT category_id_fk FOREIGN KEY (category_id) REFERENCES public.categories(id);
--

--
-- Name: products_categories product_id_fk; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

-- ALTER TABLE ONLY public.products_categories
--     ADD CONSTRAINT product_id_fk FOREIGN KEY (product_id) REFERENCES public.products(id);


--
-- PostgreSQL database dump complete
--

